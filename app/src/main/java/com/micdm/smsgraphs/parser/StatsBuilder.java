package com.micdm.smsgraphs.parser;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.micdm.smsgraphs.data.CardStats;
import com.micdm.smsgraphs.data.MonthStats;
import com.micdm.smsgraphs.data.Stats;
import com.micdm.smsgraphs.data.incomes.DepositIncome;
import com.micdm.smsgraphs.data.incomes.Income;
import com.micdm.smsgraphs.data.outcomes.Outcome;
import com.micdm.smsgraphs.data.outcomes.OutcomeTarget;
import com.micdm.smsgraphs.data.outcomes.PurchaseOutcome;
import com.micdm.smsgraphs.data.outcomes.TransactionOutcome;
import com.micdm.smsgraphs.data.outcomes.WithdrawalOutcome;

public class StatsBuilder {

    public static class BuildError extends Exception {

        public BuildError(String message) {
            super(message);
        }
    }

    private static final Uri INBOX_URI = Uri.parse("content://sms/inbox");

    private final Context context;
    private final Stats result = new Stats();
    private final OutcomeTargetManager outcomeTargetManager;

    public StatsBuilder(Context context) {
        this.context = context;
        this.outcomeTargetManager = new OutcomeTargetManager(context);
    }

    public Stats build() throws BuildError {
        String[] fields = new String[] {"body"};
        String where = "address = 900";
        Cursor cursor = context.getContentResolver().query(INBOX_URI, fields, where, null, null);
        if (cursor == null) {
            throw new BuildError("can not access to messages");
        }
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            Message message = MessageParser.parse(cursor.getString(0));
            if (message != null) {
                handleMessage(message);
            }
            cursor.moveToNext();
        }
        return result;
    }

    private void handleMessage(Message message) throws BuildError{
        CardStats cardStats = getCardStats(result, message);
        MonthStats monthStats = getMonthStats(cardStats, message);
        checkForIncome(monthStats, message);
        checkForOutcome(monthStats, message);
    }

    private CardStats getCardStats(Stats stats, Message message) {
        String title = message.getCard();
        CardStats cardStats = result.getCardStats(title);
        if (cardStats == null) {
            cardStats = new CardStats(title);
            stats.addCardStats(cardStats);
        }
        return cardStats;
    }

    private MonthStats getMonthStats(CardStats cardStats, Message message) {
        MonthStats monthStats = cardStats.getMonthStats(message.getYear(), message.getMonth());
        if (monthStats == null) {
            monthStats = new MonthStats(message.getYear(), message.getMonth());
            cardStats.addMonthStats(monthStats);
        }
        return monthStats;
    }

    private void checkForIncome(MonthStats monthStats, Message message) throws BuildError {
        if (!message.isIncome()) {
            return;
        }
        Income income = buildIncome(message);
        Income same = monthStats.getIncome(income);
        if (same == null) {
            monthStats.addIncome(income);
            income.addAmount(message.getAmount());
        } else {
            same.addAmount(message.getAmount());
        }
    }

    private Income buildIncome(Message message) throws BuildError{
        switch (message.getType()) {
            case DEPOSIT:
                return new DepositIncome();
            default:
                throw new BuildError(String.format("unknown income type %s", message.getType()));
        }
    }

    private void checkForOutcome(MonthStats monthStats, Message message) throws BuildError{
        if (!message.isOutcome()) {
            return;
        }
        Outcome outcome = buildOutcome(message);
        Outcome same = monthStats.getOutcome(outcome);
        if (same == null) {
            monthStats.addOutcome(outcome);
            outcome.addAmount(message.getAmount());
        } else {
            same.addAmount(message.getAmount());
        }
    }

    private Outcome buildOutcome(Message message) throws BuildError{
        switch (message.getType()) {
            case WITHDRAWAL:
                return new WithdrawalOutcome();
            case PURCHASE:
                OutcomeTarget target = outcomeTargetManager.get(message.getTarget());
                if (target == null) {
                    target = new OutcomeTarget(message.getTarget());
                    outcomeTargetManager.add(target);
                }
                return new PurchaseOutcome(target);
            case TRANSFER:
                return new TransactionOutcome();
            default:
                throw new BuildError(String.format("unknown outcome type %s", message.getType()));
        }
    }
}
