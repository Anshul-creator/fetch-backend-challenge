package com.example.FetchBackend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a transaction. A transaction consists of a payer, 
 * the amount of points, and the timestamp when the transaction occurred.
 *
 * Attributes:
 * - payer: The name of the payer associated with this transaction.
 * - points: The number of points involved in this transaction.
 * - timestamp: The timestamp when this transaction occurred.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String payer;
    private int points;
    private String timestamp;
}