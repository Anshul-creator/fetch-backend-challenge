package com.example.FetchBackend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class PointsController {
    
    private final List<Transaction> transactions = new LinkedList<>();
    private final Map<String, Integer> payerBalances = new HashMap<>();
    
    @PostMapping("/add")
    public void addPoints(@RequestBody Transaction transaction) {
        transactions.add(transaction);
        payerBalances.put(transaction.getPayer(), payerBalances.getOrDefault(transaction.getPayer(), 0) + transaction.getPoints());
    }
    
    @PostMapping("/spend")
    public List<SpendResult> spendPoints(@RequestBody SpendRequest spendRequest) {
        int pointsToSpend = spendRequest.getPoints();
        List<SpendResult> results = new ArrayList<>();
        
        if (pointsToSpend > getTotalPoints()) {
            throw new IllegalArgumentException("Insufficient points.");
        }
        
        transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        for (Iterator<Transaction> it = transactions.iterator(); it.hasNext() && pointsToSpend > 0;) {
            Transaction transaction = it.next();
            int pointsAvailable = Math.min(transaction.getPoints(), pointsToSpend);
            //if (pointsAvailable <= 0) continue;

            pointsToSpend -= pointsAvailable;
            int newBalance = payerBalances.get(transaction.getPayer()) - pointsAvailable;
            payerBalances.put(transaction.getPayer(), newBalance);

            results.add(new SpendResult(transaction.getPayer(), -pointsAvailable));

            transaction.setPoints(transaction.getPoints() - pointsAvailable);
            if (transaction.getPoints() == 0) it.remove();
        }

        return results;
    }
    
    @GetMapping("/balance")
    public Map<String, Integer> getBalance() {
        return payerBalances;
    }

    private int getTotalPoints() {
        return payerBalances.values().stream().mapToInt(Integer::intValue).sum();
    }
}