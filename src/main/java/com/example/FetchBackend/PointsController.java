package com.example.FetchBackend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class manages points transactions and balances. It handles the addition of points, 
 * spending points, and retrieving the balance for each payer.
 * 
 * Attributes:
 * - transactions: A list that tracks all the point transactions.
 * - payerBalances: A map that keeps track of the total balance for each payer.
 * 
 * Endpoints:
 * - /add: Adds points to a payer's balance.
 * - /spend: Deducts points based on available payer balances and returns the result.
 * - /balance: Retrieves the current balance for each payer.
 */

@RestController 
public class PointsController {
    
    private final List<Transaction> transactions = new LinkedList<>(); // Stores all transactions
    private final Map<String, Integer> payerBalances = new HashMap<>(); // Tracks payer balances
    
    /**
     * Adds points to a payer's balance.
     * 
     * @param transaction A Transaction object containing the payer, points, and timestamp.
     */
    @PostMapping("/add")
    public void addPoints(@RequestBody Transaction transaction) {
        transactions.add(transaction);
        payerBalances.put(transaction.getPayer(), payerBalances.getOrDefault(transaction.getPayer(), 0) + transaction.getPoints());
    }
    
    /**
     * Spends points across payers based on available points, spending the points with oldest timestamp first.
     * 
     * @param spendRequest A request specifying the number of points to spend.
     * @return A list of SpendResult objects telling how the points were deducted across payers.
     * @throws ResponseStatusException if there are insufficient points.
     */
    @PostMapping("/spend")
    public List<SpendResult> spendPoints(@RequestBody SpendRequest spendRequest) {
        int pointsToSpend = spendRequest.getPoints();
        List<SpendResult> results = new ArrayList<>();
        
        // Check if there are enough points to spend
        if (pointsToSpend > getTotalPoints()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient points.");
        }
        
        // Sort transactions by timestamp and spend points order of the oldest timestamp
        transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        for (Iterator<Transaction> it = transactions.iterator(); it.hasNext() && pointsToSpend > 0;) {
            Transaction transaction = it.next();
            int pointsAvailable = Math.min(transaction.getPoints(), pointsToSpend);

            pointsToSpend -= pointsAvailable;
            int newBalance = payerBalances.get(transaction.getPayer()) - pointsAvailable;
            payerBalances.put(transaction.getPayer(), newBalance);

            results.add(new SpendResult(transaction.getPayer(), -pointsAvailable));

            // Update transaction points or remove it if fully spent
            transaction.setPoints(transaction.getPoints() - pointsAvailable);
            if (transaction.getPoints() == 0) it.remove();
        }

        return results;
    }
    
    /**
     * Retrieves the current balance for each payer.
     * 
     * @return A map of payer names to their current points balance.
     */
    @GetMapping("/balance")
    public Map<String, Integer> getBalance() {
        return payerBalances;
    }

    /**
     * Calculates the total points across all payers.
     * 
     * @return The sum of points for all payers.
     */
    private int getTotalPoints() {
        return payerBalances.values().stream().mapToInt(Integer::intValue).sum();
    }
}