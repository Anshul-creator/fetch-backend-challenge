package com.example.FetchBackend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a request to spend a specific number of points.
 *
 * Attributes:
 * - points: The number of points to be spent in the transaction.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpendRequest {
    private int points;
}