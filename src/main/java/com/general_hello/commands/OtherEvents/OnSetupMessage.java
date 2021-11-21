package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.commands.Auction.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.HashMap;

public class OnSetupMessage extends ListenerAdapter {
    public static HashMap<Auction, Message> mainMessage = new HashMap<>();
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String contentRaw = event.getMessage().getContentRaw();
        TextChannel channel = event.getChannel();
        if (event.getAuthor().isBot()) return;
        if (QuestionProgress.progress.containsKey(event.getMember())) {
            Progress progress = QuestionProgress.progress.get(event.getMember());
            switch (progress) {
                case TIME:
                    try {
                        OffsetDateTime offsetDateTime = stringToTime(contentRaw);
                        QuestionProgress.time.put(event.getMember(), offsetDateTime);
                        channel.sendMessage("The auction will end in " + TimeFormat.RELATIVE.format(offsetDateTime) + "!").queue();
                        QuestionProgress.progress.put(event.getMember(), Progress.ITEM);
                        channel.sendMessage("What will be the item for this auction?").queue();
                    } catch (NumberFormatException e) {
                        channel.sendMessage("The format you placed is incorrect!").queue();
                    }
                    break;
                case ITEM:
                    QuestionProgress.item.put(event.getMember(), contentRaw);
                    QuestionProgress.progress.put(event.getMember(), Progress.MINBID);
                    channel.sendMessage("The item will be `" + contentRaw + "`!").queue();
                    channel.sendMessage("What will the minimum bid be?").queue();
                    break;
                case MINBID:
                    try {
                        QuestionProgress.minBid.put(event.getMember(), Long.parseLong(contentRaw));
                        QuestionProgress.progress.put(event.getMember(), Progress.MAXBID);
                        channel.sendMessage("The minimum bid will be `" + contentRaw + "`!").queue();
                        channel.sendMessage("What will the maximum bid be?").queue();
                    } catch (Exception e) {
                        channel.sendMessage("Invalid number placed! Try again!").queue();
                    }
                    break;
                case MAXBID:
                    try {
                        if (Integer.parseInt(contentRaw) < QuestionProgress.minBid.get(event.getMember())) {
                            channel.sendMessage("The maximum bid should be greater than the minimum bid!").queue();
                            return;
                        }
                        QuestionProgress.maxBid.put(event.getMember(), Long.parseLong(contentRaw));
                        QuestionProgress.progress.put(event.getMember(), Progress.ROLE);
                        channel.sendMessage("The maximum bid will be `" + contentRaw + "`!").queue();
                        channel.sendMessage("What will be the role that allows people to bid? (Place the name only!)").queue();
                    } catch (Exception e) {
                        channel.sendMessage("Invalid number placed! Try again!").queue();
                    }
                    break;
                case ROLE:
                    try {
                        Role role = event.getGuild().getRolesByName(contentRaw, true).get(0);
                        QuestionProgress.allowedRole.put(event.getMember(), role);
                        QuestionProgress.progress.put(event.getMember(), Progress.MAINCHANNEL);
                        channel.sendMessage("The role will be `" + role.getName() + "`!").queue();
                        channel.sendMessage("What will the channel of the auction be?").queue();
                    } catch (Exception e) {
                        channel.sendMessage("Invalid role placed! Try again!").queue();
                    }
                    break;
                case MAINCHANNEL:
                    try {
                        TextChannel textChannel = event.getGuild().getTextChannelsByName(contentRaw, true).get(0);
                        QuestionProgress.mainChannel.put(event.getMember(), textChannel);
                        QuestionProgress.progress.put(event.getMember(), Progress.LOGCHANNEL);
                        channel.sendMessage("The text channel of the auction will be `" + textChannel.getName() + "`!").queue();
                        channel.sendMessage("What will the log channel of the auction be?").queue();
                    } catch (Exception e) {
                        channel.sendMessage("Invalid channel placed! Try again!").queue();
                    }
                    break;
                case LOGCHANNEL:
                    try {
                        Member member = event.getMember();
                        TextChannel textChannel = event.getGuild().getTextChannelsByName(contentRaw, true).get(0);
                        QuestionProgress.logChannel.put(event.getMember(), textChannel);
                        QuestionProgress.progress.remove(event.getMember());
                        channel.sendMessage("The log channel of the auction will be `" + textChannel.getName() + "`!").queue();
                        channel.sendMessage("Completing the setup......").queue();

                        OffsetDateTime offsetDateTime = QuestionProgress.time.get(member);
                        TextChannel logChannel = QuestionProgress.logChannel.get(member);
                        String item = QuestionProgress.item.get(member);
                        Long maxBid = QuestionProgress.maxBid.get(member);
                        Long minBid = QuestionProgress.minBid.get(member);
                        Role role = QuestionProgress.allowedRole.get(member);
                        TextChannel mainChannel = QuestionProgress.mainChannel.get(member);
                        Auction auction = new Auction(offsetDateTime, item, mainChannel, minBid, maxBid, role, logChannel);
                        AuctionToChannelStoring.textChannelAuctionHashMap.put(mainChannel, auction);

                        channel.sendMessage("Preparing Summary......").queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("Auction for " + item);
                        embedBuilder.setDescription("Length of Auction: " + TimeFormat.RELATIVE.format(offsetDateTime) +
                                "\n" +
                                "Main channel: " + mainChannel.getAsMention() +
                                "\n" +
                                "Log channel: " + logChannel.getAsMention() +
                                "\n" +
                                "Maximum bid: " + maxBid +
                                "\n" +
                                "Minimum bid: " + minBid +
                                "\n" +
                                "Role: " + role.getAsMention());
                        embedBuilder.setColor(Color.CYAN);
                        embedBuilder.setTimestamp(OffsetDateTime.now());
                        channel.sendMessageEmbeds(embedBuilder.build()).queue();
                        int auctionId = AuctionToChannelStoring.idAuctionHashMap.size();
                        AuctionToChannelStoring.idAuctionHashMap.put(auctionId, auction);
                        sendAuctionMessage(auction, auctionId);

                        AuctionTimer.startTimerTask(auctionId);
                    } catch (Exception e) {
                        channel.sendMessage("An error occurred `" + e.getMessage() + "` Try again!").queue();
                    }
                    break;
            }
        }
    }

    private void sendAuctionMessage(Auction auction, int auctionId) {
        OffsetDateTime offsetDateTime = auction.getTime();
        TextChannel logChannel = auction.getLogChannel();
        String item = auction.getItem();
        long maxBid = auction.getMaxBid();
        long minBid = auction.getMinBid();
        Role role = auction.getAllowedRole();
        TextChannel mainChannel = auction.getMainChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Auction for " + item);
        embedBuilder.setDescription("Length of Auction: " + TimeFormat.RELATIVE.format(offsetDateTime) +
                "\n" +
                "Maximum bid: " + maxBid +
                "\n" +
                "Minimum bid: " + minBid +
                "\n" +
                "Role: " + role.getAsMention());
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setThumbnail("https://tenor.com/view/sold-auction-gif-9041957");
        embedBuilder.setTimestamp(OffsetDateTime.now());
        mainChannel.sendMessageEmbeds(embedBuilder.build())
                .setActionRows(
                        ActionRow.of(Button.primary("0000:placebid:" + auctionId, "Place Bid"))
                ).queue((message -> {
                    mainMessage.put(auction, message);
                }));

        logChannel.sendMessage("Sent the auction message in " + mainChannel.getAsMention()).queue();
    }

    private OffsetDateTime stringToTime(String time) {
        time = time.toLowerCase();
        time = time.replaceAll("\\s+", "");
        String[] owo = time.split("d");
        String days;
        String hours;
        String minutes;

        if (owo.length > 1) {
            days = owo[0];
            owo = owo[1].split("h");
        } else {
            days = null;
            owo = owo[0].split("h");
        }

        if (owo.length > 1) {
            hours = owo[0];
            owo = owo[1].split("m");
        } else {
            hours = null;
            owo = owo[0].split("m");
        }

        if (owo.length > 0) {
            minutes = owo[0];
        } else {
            minutes = null;
        }

        System.out.println(days);
        System.out.println(hours);
        System.out.println(minutes);

        return OffsetDateTime.now().plusDays(toLong(days)).plusHours(toLong(hours)).plusMinutes(toLong(minutes));
    }

    private long toLong(String string) {
        if (string == null) {
            return 0L;
        }
        return Long.parseLong(string);
    }
}
