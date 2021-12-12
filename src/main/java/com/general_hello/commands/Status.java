package com.general_hello.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class Status {
    public Thread status() {
        Runnable runnable = () -> {
            while (true) {
                JDA jda = Bot.jda;

                jda.getPresence().setActivity(Activity.listening("Dashing through the snow"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("In a one-horse open sleigh"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("O'er the fields we go"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Laughing all the way."));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Bells on bob-tails ring"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Making spirits bright"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("What fun it is to ride and sing"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("A sleighing song tonight, oh"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Jingle Bells, Jingle Bells"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Jingle all the way"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Oh what fun it is to ride"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("In a one-horse open sleigh, hey!"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Jingle Bells, Jingle Bells"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Jingle all the way"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Oh what fun it is to ride"));

                try {
                    Thread.sleep(6000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("In a one-horse open sleigh"));

                /*System.out.println("Yeet");
                jda.getPresence().setActivity(Activity.listening("Made by General Koala#0400"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.competing("This bot uses over 10,000 lines of code"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.watching("ignt help"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Made by HELLO66#0066"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Emojis made by TwoTonTan#9336"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Design made by SkyacinthClues#9536"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("Emojis made by kitkatsnickers#3901"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.listening("The Passion of Christ"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }

                jda.getPresence().setActivity(Activity.streaming("Merry Christmas! 🎄🎄🎄", "discord.com"));

                try {
                    Thread.sleep(1000000);
                } catch (Exception ignored) {
                }*/
            }
        };

        return new Thread(runnable);
    }
}
