package com.suhaib.swiftpos.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suhaib.swiftpos.common.model.StoreInfo
import com.suhaib.swiftpos.common.model.TransactionItem
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus
import com.suhaib.swiftpos.common.utils.DateUtils
import com.suhaib.swiftpos.common.utils.getNDecimalPoint
import com.suhaib.swiftpos.ui.theme.SwiftPOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SwiftPOSTheme(darkTheme = false) {
                homeViewModel = viewModel<HomeViewModel>()
                Surface(
                    modifier = Modifier
//                        .aspectRatio(16f / 9f)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(homeViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(homeViewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CheckoutScreen(homeViewModel)
        OrderScreen(homeViewModel)
        when(homeViewModel.isTransactionDialogDisplay.collectAsStateWithLifecycle().value) {
            TransactionStatus.SAVED -> TransactionDialog(homeViewModel)
            TransactionStatus.COMPLETED -> TransactionDialog(homeViewModel)
            null -> {}
        }
    }
}

@Composable
fun OrderScreen(homeViewModel: HomeViewModel) {
    Column(
        Modifier
            .background(Color(0xFFE1E1E1))
            .fillMaxHeight()
    ) {
        OrderInfoBar(homeViewModel.currentStore.collectAsStateWithLifecycle().value)
        PriceBookItemGrid(homeViewModel)
        Spacer(modifier = Modifier.weight(1f))
        OrderActionBar(homeViewModel)
    }
}

@Composable
fun OrderInfoBar(storeInfo: StoreInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(84.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color(0xFFF6F6F6))
            .padding(start = 21.dp, top = 5.dp, end = 21.dp, bottom = 10.dp)
    ) {
        Column {
            Text(
                text = "Total sales",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191B1E),
                )
            )
            Text(
                text = "$" + storeInfo.totalSalesAmount,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF191B1E),
                )
            )
        }
        Column {
            Text(
                text = "Customer Count",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191B1E),
                )
            )
            Text(
                text = storeInfo.numberOfCustomers.toString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF191B1E),
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "Store Name",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191B1E),
                )
            )
            Text(
                text = storeInfo.storeName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF191B1E),
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PriceBookItemGrid(homeViewModel: HomeViewModel) {
    val storeItemList = homeViewModel.listOfPriceBookItems.collectAsStateWithLifecycle().value
    LazyVerticalGrid(
        GridCells.Fixed(5),
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize()
    ) {
        items(storeItemList) { currentItem ->
            Button(
                onClick = {
                    homeViewModel.updateCurrentTransactionListAndRecalculateTotal(currentItem)
                },
                shape = RoundedCornerShape(2.dp),
                elevation = ButtonDefaults.buttonElevation(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF513DA3), contentColor = Color.White),
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = currentItem.itemName,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 26.dp)
                        .basicMarquee(6)
                )
            }
        }
    }
}

@Composable
private fun OrderActionBar(homeViewModel: HomeViewModel) {
    val transactionItemsList = homeViewModel.currentTransactionItem.collectAsStateWithLifecycle().value
    val modifier = Modifier
        .padding(0.dp, 10.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color(0xFFF6F6F6))
            .padding(start = 14.dp, top = 5.dp, end = 21.dp, bottom = 5.dp)
    ) {
        if (transactionItemsList.isNotEmpty()) {
            Button(
                onClick = {
                    homeViewModel.saveCurrentCartItems()
                },
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A85FF), contentColor = Color.White),
            ) {
                Text(
                    text = "Save",
                    fontSize = 20.sp,
                    modifier = modifier
                )
            }
        }
        Button(
            onClick = {
                homeViewModel.showTransactionDialogAndGetTransactions(TransactionStatus.SAVED)
            },
            shape = RoundedCornerShape(6.dp),
            elevation = ButtonDefaults.buttonElevation(3.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2B2B2B), contentColor = Color.White),
        ) {
            Text(
                text = "Recall",
                fontSize = 20.sp,
                modifier = modifier
            )
        }
        Button(
            onClick = {
                homeViewModel.showTransactionDialogAndGetTransactions(TransactionStatus.COMPLETED)
            },
            shape = RoundedCornerShape(6.dp),
            elevation = ButtonDefaults.buttonElevation(3.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF513DA3), contentColor = Color.White),
        ) {
            Text(
                text = "Txn History",
                fontSize = 20.sp,
                modifier = modifier
            )
        }
        if (transactionItemsList.isNotEmpty()) {
            Button(
                onClick = {
                    homeViewModel.buyCurrentCartItems()
                },
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF4BD461), contentColor = Color.White),
            ) {
                Text(
                    text = "Complete",
                    fontSize = 20.sp,
                    modifier = modifier
                )
            }
        }
        val showAlertDialog = remember { mutableStateOf(false) }
        if (transactionItemsList.isNotEmpty()) {
            Button(
                onClick = {
                    showAlertDialog.value = true
                },
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFCA5858), contentColor = Color.White),
            ) {
                Text(
                    text = "Clear",
                    fontSize = 20.sp,
                    modifier = modifier
                )
                if (showAlertDialog.value) {
                    ShowAlertDialog(text = "Clear Cart", description = "Are you sure you want to clear the cart?", showAlertDialog) {
                        homeViewModel.clearCartAndRecalculateTotal()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = DateUtils.convertEpochToDateTime(System.currentTimeMillis()),
            fontSize = 22.sp,
            modifier = Modifier
                .padding(10.dp, 10.dp)
        )
    }
}

@Composable
private fun CheckoutScreen(homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .width(460.dp)
            .fillMaxHeight()
            .background(color = Color(0xFFF6F6F6)),
    ) {
        CheckoutTopBar()
        CheckoutCartScreen(homeViewModel)
        Spacer(modifier = Modifier.weight(1f))
        CheckoutBottomBar(homeViewModel)
    }
}

@Composable
private fun CheckoutTopBar() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(84.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .border(width = 2.dp, color = Color(0xFFD8D8D8))
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
            .padding(start = 21.dp, top = 26.dp, end = 21.dp, bottom = 26.dp)
    ) {
        Text(
            text = "Description",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "QTY",
            modifier = Modifier
                .padding(start = 20.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "Amount",
            modifier = Modifier
                .padding(start = 10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
    }
}

@Composable
private fun CheckoutCartScreen(homeViewModel: HomeViewModel) {
    val currentTransactionList = homeViewModel.currentTransactionItem.collectAsStateWithLifecycle().value
    if (currentTransactionList.isEmpty()) {
        ShowEmptyCartScreen()
    } else {
        CheckoutCartItems(currentTransactionList)
    }
}

@Composable
private fun ShowEmptyCartScreen() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        text = "Your cart is empty.\nPlease add items to Purchase with us",
        textAlign = TextAlign.Center,
        fontSize = 18.sp
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun CheckoutCartItems(currentTransactionList: List<TransactionItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize()
    ) {
        items(currentTransactionList) { transactionItem ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
            ) {
                val amount = (transactionItem.pbItem.price * transactionItem.quantity)
                Text(
                    text = transactionItem.pbItem.itemName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF272727),
                    maxLines = 2,
                    modifier = Modifier
                        .width(180.dp)
                        .basicMarquee(6)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(50.dp)
                        .background(color = Color(0xFFE3E3E3), shape = RoundedCornerShape(size = 100.dp))
                        .padding(start = 10.dp, top = 8.dp, end = 10.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "x${transactionItem.quantity}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            color = Color(0xFF5C5C5C),
                        )
                    )
                }
                Text(
                    text = amount.getNDecimalPoint(2).toString(),
                    modifier = Modifier
                        .padding(start = 70.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF272727),
                        textAlign = TextAlign.Start,
                    )
                )
            }
        }
    }
}

@Composable
private fun CheckoutBottomBar(homeViewModel: HomeViewModel) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .border(width = 2.dp, color = Color(0xFFD8D8D8), shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
            .padding(start = 21.dp, top = 26.dp, end = 21.dp, bottom = 26.dp)
    ) {
        CheckoutFinalCalculationRow(
            text = "Sub Total",
            value = homeViewModel.transactionSubTotalAmount.collectAsStateWithLifecycle().value,
            Modifier,
            color = Color(0xFF5C5C5C),
        )
        CheckoutFinalCalculationRow(
            text = "Tax",
            value = homeViewModel.transactionTaxes.collectAsStateWithLifecycle().value,
            Modifier,
            color = Color(0xFF5C5C5C),
        )
        CheckoutFinalCalculationRow(
            text = "Discount",
            value = homeViewModel.transactionTotalDiscount.collectAsStateWithLifecycle().value,
            Modifier,
            Color(0xFF4BD461)
        )
        CheckoutFinalCalculationRow(
            text = "Grand Total",
            value = homeViewModel.transactionGrandTotal.collectAsStateWithLifecycle().value,
            Modifier,
            color = Color(0xFF191B1E),
            FontWeight(700),
        )
    }
}

@Composable
private fun CheckoutFinalCalculationRow(text: String, value: Float, modifier: Modifier, color: Color, fontWeight: FontWeight = FontWeight(500)) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            color = color,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = fontWeight,
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "$$value",
            color = color,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = fontWeight,
                color = Color(0xFF191B1E),
            )
        )
    }
}

@Composable
fun ShowAlertDialog(text: String, description: String, showAlertDialog: MutableState<Boolean>, onConfirm: () -> Unit) {
    AlertDialog(
        title = { Text(text = text) },
        text = { Text(text = description) },
        onDismissRequest = {
            showAlertDialog.value = false
        },
        dismissButton = {
            Button(
                onClick = {
                    showAlertDialog.value = false
                }) {
                Text("No")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    showAlertDialog.value = false
                }) {
                Text("Yes")
            }
        })
}

//@Preview(showBackground = true, device = "spec:width=1920dp,height=1080dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
//@Composable
//fun GreetingPreview() {
//    SwiftPOSTheme {
//        MainView(mutableStateOf(listOf()))
//    }
//}
