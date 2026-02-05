/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package coursework.skmotorsauction;

/**
 *
 * @author brix
 */
import java.util.Scanner;

public class SKMotorsAuction {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Vehicle details
        System.out.print("Enter Vehicle Registration Number: ");
        String regNumber = input.nextLine();

        System.out.print("Enter Vehicle Cost: ");
        double vehicleCost = input.nextDouble();

        System.out.print("Enter Total Deposits Made: ");
        double deposits = input.nextDouble();

        System.out.print("Enter Total Expenses Incurred: ");
        double expenses = input.nextDouble();

        System.out.print("Enter Balance Left on Vehicle: ");
        double balance = input.nextDouble();

        // Bidding section
        double highestBid = 0;
        int highestBidder = 0;

        for (int i = 1; i <= 3; i++) {
            System.out.print("Enter bid amount for Bidder " + i + ": ");
            double bid = input.nextDouble();

            if (bid > highestBid) {
                highestBid = bid;
                highestBidder = i;
            }
        }

        // Profit / Loss calculation after balance is cleared
        double totalIncome = highestBid + deposits;
        double totalCost = vehicleCost + expenses;
        double profitOrLoss = totalIncome - totalCost;

        // Output section
        System.out.println("\n----- AUCTION RESULTS -----");
        System.out.println("Vehicle Registration Number: " + regNumber);
        System.out.println("Winning Bidder: Bidder " + highestBidder);
        System.out.println("Winning Bid Amount: " + highestBid);
        System.out.println("Balance Cleared: " + balance);

        System.out.println("\n----- FINANCIAL SUMMARY -----");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Cost: " + totalCost);

        if (profitOrLoss > 0) {
            System.out.println("PROFIT Made: " + profitOrLoss);
        } else if (profitOrLoss < 0) {
            System.out.println("LOSS Incurred: " + Math.abs(profitOrLoss));
        } else {
            System.out.println("No Profit, No Loss");
        }

        input.close();
    }
}

