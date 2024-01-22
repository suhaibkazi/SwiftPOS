package com.suhaib.swiftpos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suhaib.swiftpos.App
import com.suhaib.swiftpos.common.di.dispatcher.DispatcherProvider
import com.suhaib.swiftpos.common.model.PriceBookItem
import com.suhaib.swiftpos.common.model.StoreInfo
import com.suhaib.swiftpos.common.model.StoreTransaction
import com.suhaib.swiftpos.common.model.TransactionItem
import com.suhaib.swiftpos.common.model.TransactionWithItemsAndStoreInfo
import com.suhaib.swiftpos.common.repository.StoreRepository
import com.suhaib.swiftpos.common.repository.TransactionRepository
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus
import com.suhaib.swiftpos.common.utils.ReadJSONFromAssets
import com.suhaib.swiftpos.common.utils.getNDecimalPoint
import com.suhaib.swiftpos.common.utils.toDataClassObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val storeRepository: StoreRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private var _listOfPriceBookItems = MutableStateFlow<List<PriceBookItem>>(listOf())
    val listOfPriceBookItems = _listOfPriceBookItems.asStateFlow()

    private var _currentStore = MutableStateFlow(StoreInfo())
    val currentStore = _currentStore.asStateFlow()

    var currentTransactionItem = MutableStateFlow<List<TransactionItem>>(listOf())
    var transactionSubTotalAmount = MutableStateFlow(0F)
    var transactionTotalDiscount = MutableStateFlow(0F)
    var transactionTaxes = MutableStateFlow(0F)
    var transactionGrandTotal = MutableStateFlow(0F)
    var currentTransactionID = MutableStateFlow(Random(System.currentTimeMillis()).nextInt(0..999999))
    var isTransactionDialogDisplay = MutableStateFlow<TransactionStatus?>(null)
    var transactionsWithItemsAndStoreInfo = MutableStateFlow<List<TransactionWithItemsAndStoreInfo>>(listOf())

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            storeRepository.allStores.collectLatest {
                if (it.isEmpty()) {
                    storeRepository.insertStoreInfo(
                        StoreInfo(
                            storeName = "Store No: ${Random.nextInt(100, 1000)}",
                            discountValues = Pair(25, 10)
                        )
                    )
                } else {
                    _currentStore.value = it.first()
                    getPriceBookItemsByStoreID(_currentStore.value.storeID)
                }
            }
        }
    }

    private fun getPriceBookItemsByStoreID(storeID: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            storeRepository.getPriceBookItemsByStoreID(storeID).collectLatest {
                if (it.isEmpty()) {
                    insertPriceBookItemFromJSON(storeID)
                }
                launch(dispatcherProvider.main) {
                    _listOfPriceBookItems.value = it
                }
            }
        }
    }

    private fun insertPriceBookItemFromJSON(storeID: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            val json = ReadJSONFromAssets(App.context, "price_book_json.json")
            val priceBookItemList = json.toDataClassObject<List<PriceBookItem>>()
            priceBookItemList.map {
                storeRepository.insertPriceBookItem(it.copy(storeID = storeID))
            }
        }
    }

    fun updateCurrentTransactionListAndRecalculateTotal(currentItem: PriceBookItem) {
        viewModelScope.launch(dispatcherProvider.io) {
            updateCurrentTransactionList(currentItem)
            recalculateTotal()
        }
    }

    private fun updateCurrentTransactionList(currentItem: PriceBookItem) {
        val existingItemIndex = currentTransactionItem.value.indexOfFirst { it.pbItem.pbID == currentItem.pbID }
        val updatedList = currentTransactionItem.value.toMutableList()
        if (existingItemIndex != -1) {
            val existingItem = currentTransactionItem.value[existingItemIndex]
            updatedList[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
            currentTransactionItem.value = updatedList.toList()
        } else {
            val newItem = TransactionItem(pbItem = currentItem, quantity = 1)
            currentTransactionItem.value += newItem
        }
    }

    private fun recalculateTotal() {
        transactionSubTotalAmount.value = currentTransactionItem.value.map { it.pbItem.price * it.quantity }.sum().getNDecimalPoint(2)
        transactionTotalDiscount.value = getTotalDiscount(transactionSubTotalAmount.value)
        transactionTaxes.value = getTotalTaxes()
        transactionGrandTotal.value = (transactionSubTotalAmount.value + transactionTaxes.value - transactionTotalDiscount.value).getNDecimalPoint(2)
    }

    private fun getTotalTaxes() = currentTransactionItem.value.sumOf { transactionItem ->
        val taxRates = transactionItem.pbItem.taxRates
        val chosenTaxRate = when {
            transactionItem.quantity > 7 && taxRates.size == 2 -> taxRates[1]
            taxRates.size == 1 -> taxRates[0]
            else -> 0f
        }
        (transactionItem.quantity * transactionItem.pbItem.price * chosenTaxRate) / 100.0
    }.getNDecimalPoint(2).toFloat()

    private fun getTotalDiscount(transactionSubTotalAmount: Float) = if (transactionSubTotalAmount in 100F..200F) {
        currentStore.value.discountValues.first.toFloat()
    } else if (transactionSubTotalAmount > 200F) {
        (transactionSubTotalAmount * currentStore.value.discountValues.second) / 100
    } else {
        0F
    }.getNDecimalPoint(2)

    fun clearCartAndRecalculateTotal() {
        currentTransactionID = MutableStateFlow(Random(System.currentTimeMillis()).nextInt(0..999999))
        currentTransactionItem.value = emptyList()
        recalculateTotal()
    }

    fun saveCurrentCartItems() {
        viewModelScope.launch(dispatcherProvider.io) {
            val storeTransaction = StoreTransaction(
                transactionID = currentTransactionID.value,
                transactionStartTime = System.currentTimeMillis(),
                transactionSubTotalAmount = transactionSubTotalAmount.value.getNDecimalPoint(2),
                transactionTotalDiscountAmount = transactionTotalDiscount.value.getNDecimalPoint(2),
                transactionTotalTaxAmount = transactionTaxes.value.getNDecimalPoint(2),
                transactionGrandTotalAmount = transactionGrandTotal.value.getNDecimalPoint(2),
                transactionStatus = TransactionStatus.SAVED,
                storeID = currentStore.value.storeID,
            )
            transactionRepository.upsertTransactionAndTransactionItemsToDB(
                storeTransaction,
                currentTransactionItem.value.map { it.copy(transactionID = currentTransactionID.value) })
            clearCartAndRecalculateTotal()
        }
    }

    fun buyCurrentCartItems() {
        viewModelScope.launch(dispatcherProvider.io) {
            val storeTransaction = StoreTransaction(
                transactionID = currentTransactionID.value,
                transactionStartTime = System.currentTimeMillis(),
                transactionEndTime = System.currentTimeMillis(),
                transactionSubTotalAmount = transactionSubTotalAmount.value.getNDecimalPoint(2),
                transactionTotalDiscountAmount = transactionTotalDiscount.value.getNDecimalPoint(2),
                transactionTotalTaxAmount = transactionTaxes.value.getNDecimalPoint(2),
                transactionGrandTotalAmount = transactionGrandTotal.value.getNDecimalPoint(2),
                transactionStatus = TransactionStatus.COMPLETED,
                storeID = currentStore.value.storeID,
            )
            transactionRepository.upsertTransactionAndTransactionItemsToDB(
                storeTransaction,
                currentTransactionItem.value.map { it.copy(transactionID = currentTransactionID.value) })
            clearCartAndRecalculateTotal()
        }
    }

    fun showTransactionDialogAndGetTransactions(transactionStatus: TransactionStatus) {
        isTransactionDialogDisplay.value = transactionStatus
        viewModelScope.launch(dispatcherProvider.io) {
            transactionRepository.getTransactionItemsByStoreIDAndTransactionStatus(currentStore.value.storeID, transactionStatus).collectLatest {
                launch(dispatcherProvider.main) {
                    transactionsWithItemsAndStoreInfo.value = it
                }
            }
        }
    }

    fun loadRecalledTransactionItems(transactionItem: TransactionWithItemsAndStoreInfo) {
        clearCartAndRecalculateTotal()
        isTransactionDialogDisplay.value = null
        currentTransactionItem.value = transactionItem.transactionItems
        currentTransactionID.value = transactionItem.storeTransaction.transactionID
        recalculateTotal()
    }
}