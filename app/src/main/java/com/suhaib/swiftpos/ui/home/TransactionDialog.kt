package com.suhaib.swiftpos.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suhaib.swiftpos.R
import com.suhaib.swiftpos.common.model.TransactionWithItemsAndStoreInfo
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus
import com.suhaib.swiftpos.common.utils.DateUtils

@Composable
fun TransactionDialog(homeViewModel: HomeViewModel) {
    Dialog(
        onDismissRequest = {
            homeViewModel.isTransactionDialogDisplay.value = null
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        DialogScreen(homeViewModel)
    }
}

@Composable
private fun DialogScreen(
    homeViewModel: HomeViewModel,
) {
    val transactionsWithItemsAndStoreInfo = homeViewModel.transactionsWithItemsAndStoreInfo.collectAsStateWithLifecycle().value
    Column(
        modifier = Modifier
            .fillMaxWidth(0.77f)
            .padding(15.dp)
    ) {
        DialogTopBar(homeViewModel)
        TitleBar()
        if (transactionsWithItemsAndStoreInfo.isNotEmpty()) {
            TransactionList(transactionsWithItemsAndStoreInfo, homeViewModel)
        } else {
            ShowEmptyTransactionScreen()
        }
    }
}

@Composable
private fun ShowEmptyTransactionScreen() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 120.dp),
        text = "Your have not made any transaction.\nPlease save a transaction or buy any item",
        textAlign = TextAlign.Center,
        fontSize = 18.sp
    )
}

@Composable
private fun DialogTopBar(homeViewModel: HomeViewModel) {
    val text =
        if (homeViewModel.isTransactionDialogDisplay.collectAsStateWithLifecycle().value == TransactionStatus.SAVED)
            "Transaction Recall"
        else
            "Transaction History"
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .border(width = 2.dp, color = Color(0xFFD8D8D8))
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color(0xFFF6F6F6))
            .padding(start = 21.dp, top = 26.dp, end = 21.dp, bottom = 26.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF191B1E),
            )
        )
        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_close_square_24_dp),
            contentDescription = "Close button",
            modifier =
            Modifier.clickable {
                homeViewModel.isTransactionDialogDisplay.value = null
            })
    }
}

@Composable
fun TitleBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFFD8D8D8))
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Text(
            text = "Transaction No",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "Time",
            modifier = Modifier
                .padding(start = 20.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "#Items",
            modifier = Modifier
                .padding(start = 10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
        Text(
            text = "Total Amount",
            modifier = Modifier
                .padding(horizontal = 10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF191B1E),
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(transactionsWithItemsAndStoreInfo: List<TransactionWithItemsAndStoreInfo>, homeViewModel: HomeViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
    ) {
        items(transactionsWithItemsAndStoreInfo) { transactionItem ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(width = .5.dp, color = Color(0xFFD8D8D8))
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = transactionItem.storeTransaction.transactionID.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF272727),
                    maxLines = 2,
                    modifier = Modifier
                        .width(300.dp)
                        .basicMarquee(6)
                )
                Text(
                    text = DateUtils.convertEpochToDateTimeFormatted(transactionItem.storeTransaction.transactionStartTime),
                    modifier = Modifier
                        .width(270.dp)
                        .basicMarquee(6),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF5C5C5C),
                    )
                )
                Text(
                    text = transactionItem.transactionItems.map { it.quantity }.sum().toString(),
                    modifier = Modifier
                        .padding(start = 70.dp)
                        .width(40.dp)
                        .basicMarquee(6),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF272727),
                        textAlign = TextAlign.Start,
                    )
                )
                Text(
                    text = transactionItem.storeTransaction.transactionGrandTotalAmount.toString(),
                    modifier = Modifier
                        .padding(start = 250.dp)
                        .width(70.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF272727),
                        textAlign = TextAlign.Start,
                    )
                )
                if (transactionItem.storeTransaction.transactionStatus == TransactionStatus.SAVED) {
                    val showConfirmDialog = remember { mutableStateOf(false) }
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right_24_dp),
                        contentDescription = "Select button",
                        modifier =
                        Modifier
                            .height(16.dp)
                            .clickable {
                                if (homeViewModel.currentTransactionItem.value.isNotEmpty()) {
                                    showConfirmDialog.value = true
                                } else {
                                    homeViewModel.loadRecalledTransactionItems(transactionItem)
                                }
                            })

                    if (showConfirmDialog.value) {
                        ShowAlertDialog(
                            text = "Choose transaction?",
                            description = "This action with clear any unsaved transaction and recall this transaction",
                            showAlertDialog = showConfirmDialog
                        ) {
                            homeViewModel.loadRecalledTransactionItems(transactionItem)
                        }
                    }
                }
            }
        }
    }
}