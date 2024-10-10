package com.example.FetchBackend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents the result of spending points, including the payer and the points deducted.
 *
 * Attributes:
 * - payer: The name of the payer from whom the points were deducted.
 * - points: The number of points that were spent.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpendResult {
    private String payer;
    private int points;
}