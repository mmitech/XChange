/**
 * Copyright (C) 2012 - 2014 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.coinbase.dto.account;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseAccountChange.CoinbaseCache;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseTransaction.CoinbaseTransactionStatus;
import com.xeiam.xchange.coinbase.dto.marketdata.CoinbaseMoney;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseMerchant;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.utils.DateUtils;

public class CoinbaseAccountTests {

  @Test
  public void testDeserializeAccountChanges() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-account-changes-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseAccountChanges accountChanges = mapper.readValue(is, CoinbaseAccountChanges.class);

    CoinbaseUser currentUser = accountChanges.getCurrentUser();
    assertThat(currentUser.getId()).isEqualTo("527d2a1ffedcb8b73b000028");
    assertThat(currentUser.getEmail()).isEqualTo("test@test.com");
    assertThat(currentUser.getName()).isEqualTo("first last");

    assertThat(accountChanges.getBalance().getAmount()).isEqualTo(MoneyUtils.parse("BTC 7.10000000"));

    assertThat(accountChanges.getTotalCount()).isEqualTo(2);
    assertThat(accountChanges.getNumPages()).isEqualTo(1);
    assertThat(accountChanges.getCurrentPage()).isEqualTo(1);

    List<CoinbaseAccountChange> accountChangeList = accountChanges.getAccountChanges();
    assertThat(accountChangeList.size()).isEqualTo(2);

    CoinbaseAccountChange accountChange = accountChangeList.get(0);
    assertThat(accountChange.getId()).isEqualTo("52f4411aabf9534a02000085");
    assertThat(accountChange.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2014-02-06T18:12:42-08:00"));
    assertThat(accountChange.getTransactionId()).isEqualTo("52f4411aabf9534a02000081");
    assertThat(accountChange.isConfirmed()).isTrue();
    assertThat(accountChange.getAmount().getAmount()).isEqualTo(MoneyUtils.parse("BTC 1.20000000"));

    CoinbaseCache cache = accountChange.getCache();
    assertThat(cache.isNotesPresent()).isTrue();
    assertThat(cache.getCategory()).isEqualTo(CoinbaseAccountChangeCategory.TX);

    CoinbaseUser otherUser = cache.getOtherUser();
    assertThat(otherUser.getId()).isEqualTo("5083a5410924750200000001");
    assertThat(otherUser.getEmail()).isEqualTo("transfers@coinbase.com");
    assertThat(otherUser.getName()).isEqualTo("Coinbase");
  }

  @Test
  public void testDeserializeUsers() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-users-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseUsers users = mapper.readValue(is, CoinbaseUsers.class);

    List<CoinbaseUser> userList = users.getUsers();
    assertThat(userList.size()).isEqualTo(1);

    CoinbaseUser user = userList.get(0);
    assertThat(user.getId()).isEqualTo("527d2a1ffedcb8b73b000028");
    assertThat(user.getName()).isEqualTo("First Last");
    assertThat(user.getEmail()).isEqualTo("demo@demo.com");
    assertThat(user.getTimeZone()).isEqualTo("Pacific Time (US & Canada)");
    assertThat(user.getNativeCurrency()).isEqualTo("USD");
    assertThat(user.getBuyLevel()).isEqualTo(CoinbaseBuySellLevel.TWO);
    assertThat(user.getSellLevel()).isEqualTo(CoinbaseBuySellLevel.TWO);
    assertThat(user.getBalance()).isEqualTo(MoneyUtils.parse("BTC 7.10770000"));
    assertThat(user.getBuyLimit()).isEqualTo(MoneyUtils.parse("BTC 79.20000000"));
    assertThat(user.getSellLimit()).isEqualTo(MoneyUtils.parse("BTC 79.20000000"));

    CoinbaseMerchant merchant = user.getMerchant();
    assertThat(merchant.getCompanyName()).isEqualTo("XChange Demo");
    assertThat(merchant.getLogo()).isNull();
  }

  @Test
  public void testDeserializeBalance() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-balance-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseMoney balance = mapper.readValue(is, CoinbaseMoney.class);

    assertThat(balance.getAmount()).isEqualTo(MoneyUtils.parse("BTC 7.10000000"));
  }

  @Test
  public void testDeserializeAddresses() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-addresses-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseAddresses addresses = mapper.readValue(is, CoinbaseAddresses.class);

    List<CoinbaseAddress> addressList = addresses.getAddresses();
    assertThat(addressList.size()).isEqualTo(2);

    CoinbaseAddress address = addressList.get(0);
    assertThat(address.getAddress()).isEqualTo("1LXZPr7eXqM6FqjY9qoTY9PsWJ7NftCPAN");
    assertThat(address.getCallbackUrl()).isEqualTo("null");
    assertThat(address.getLabel()).isEqualTo("null");
    assertThat(address.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2014-02-14T16:50:18-08:00"));

    address = addressList.get(1);
    assertThat(address.getAddress()).isEqualTo("19mEG3d1QDch24mcDkwgfjzRYfEGpyMoQr");
    assertThat(address.getCallbackUrl()).isEmpty();
    assertThat(address.getLabel()).isEqualTo("blockchain");
    assertThat(address.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2013-11-08T16:59:57-08:00"));

    assertThat(addresses.getTotalCount()).isEqualTo(2);
    assertThat(addresses.getNumPages()).isEqualTo(1);
    assertThat(addresses.getCurrentPage()).isEqualTo(1);
  }

  @Test
  public void testDeserializeContacts() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-contacts-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseContacts contacts = mapper.readValue(is, CoinbaseContacts.class);

    List<CoinbaseContact> contactList = contacts.getContacts();
    assertThat(contactList.size()).isEqualTo(1);

    CoinbaseContact contact = contactList.get(0);
    assertThat(contact.getEmail()).isEqualTo("test@test.com");

    assertThat(contacts.getTotalCount()).isEqualTo(1);
    assertThat(contacts.getNumPages()).isEqualTo(1);
    assertThat(contacts.getCurrentPage()).isEqualTo(1);
  }

  @Test
  public void testDeserializeTransactions() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseAccountTests.class.getResourceAsStream("/account/example-transactions-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseTransactions transactions = mapper.readValue(is, CoinbaseTransactions.class);

    List<CoinbaseTransaction> transactionList = transactions.getTransactions();
    assertThat(transactionList.size()).isEqualTo(2);

    CoinbaseTransaction transaction = transactionList.get(0);
    assertThat(transaction.getId()).isEqualTo("52d8d8685a62c7613e000277");
    assertThat(transaction.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2014-01-16T23:14:48-08:00"));
    assertThat(transaction.getTransactionHash()).isNull();
    assertThat(transaction.getAmount().getAmount()).isEqualTo(MoneyUtils.parse("BTC 1.00000000"));
    assertThat(transaction.isRequest()).isFalse();
    assertThat(transaction.getStatus()).isEqualTo(CoinbaseTransactionStatus.COMPLETE);

    CoinbaseUser sender = transaction.getSender();
    assertThat(sender.getId()).isEqualTo("5083a5410924750200000001");
    assertThat(sender.getEmail()).isEqualTo("transfers@coinbase.com");
    assertThat(sender.getName()).isEqualTo("Coinbase");

    CoinbaseUser recipient = transaction.getRecipient();
    assertThat(recipient.getId()).isEqualTo("527d2a1ffedcb8b73b000028");
    assertThat(recipient.getEmail()).isEqualTo("xchange@demo.com");
    assertThat(recipient.getName()).isEqualTo("First Last");

    assertThat(transaction.getRecipientAddress()).isEqualTo("xchange@demo.com");
    assertThat(transaction.getNotes()).isEqualTo("notes");
    assertThat(transaction.getIdempotencyKey()).isEmpty();
  }
}
