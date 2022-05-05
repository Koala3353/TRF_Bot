package com.general_hello.commands.Objects;

import com.general_hello.Bot;
import com.general_hello.Config;
import com.general_hello.commands.Objects.Level.Level;
import net.dv8tion.jda.api.entities.Member;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    Date now;
    @Override
    public void run() {
        now = new Date();
        List<Member> members = Bot.jda.getGuildById(Config.get("guild")).getMembers();
        for (Member member : members) {
            new Player(member.getIdLong(), Level.PC_LV_125_FOR_HONOR).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PC_LV_150_FOR_HONOR).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PC_LV_125_SURVIVAL).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PC_LV_150_SURVIVAL).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PS_LV_125_FOR_HONOR).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PS_LV_150_FOR_HONOR).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PS_LV_125_SURVIVAL).resetGamesFoughtToday();
            new Player(member.getIdLong(), Level.PS_LV_150_SURVIVAL).resetGamesFoughtToday();
        }
    }
}
