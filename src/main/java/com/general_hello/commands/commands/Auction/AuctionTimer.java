package com.general_hello.commands.commands.Auction;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.Emoji.Emojis;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionTimer {
    public static void startTimerTask(int auctionId) {
        Auction auction = AuctionToChannelStoring.idAuctionHashMap.get(auctionId);
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                send(auction);
            }
        };
        System.out.println("Scheduled!!!");

        t.schedule(tt, Date.from(auction.getTime().toInstant()));
    }

    private static void send(Auction auction) {
        HashMap<User, Long> bids = new HashMap<>();
        for(int i = 0; i < auction.getBidders().size(); i++){
            bids.put(auction.getBidders().get(i), auction.getBid().get(i));
        }
        User winner = null;
        long highestbid = 0;
        for(User u : bids.keySet()) {
            if(bids.get(u) > highestbid){
                winner = u;
                highestbid = bids.get(u);
            }
        }

        DatabaseManager.INSTANCE.setCredits(winner.getIdLong(), (-Math.toIntExact(bids.get(winner))));

        auction.getMainChannel().sendMessage("Congratulations " + winner.getAsMention() + " you won the auction for **" + auction.getItem() + "** by bidding " + Emojis.credits + "** " + bids.get(winner) +"**!").queue();
    }
}
