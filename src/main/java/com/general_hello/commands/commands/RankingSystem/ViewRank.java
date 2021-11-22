package com.general_hello.commands.commands.RankingSystem;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.GetData;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class ViewRank implements ICommand
{
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User member = ctx.getAuthor();

        if (!ctx.getMessage().getMentionedMembers().isEmpty()) {
            member = ctx.getMessage().getMentionedUsers().get(0);
        }

        /*try{
            ByteArrayOutputStream baos = LevelPointManager.getLevelPointCard(member).getByteArrayOutputStream();
            ctx.getEvent().getChannel().sendFile(baos.toByteArray(), member.getName() + "-stats.png").queue();
        }
        catch(Exception e){
            ErrorUtils.error(ctx, e);
        }*/

        String name = member.getName().replaceAll("\\s++", "%20");

        long levelI = LevelPointManager.calculateLevel(member);
        long max = LevelPointManager.calculateLevelMax(levelI);
        long cur = GetData.getLevelPoints(member);

        String urlString = "https://app.resetxd.repl.co/level?" +
                "background=https://png.pngtree.com/thumb_back/fh260/background/20200714/pngtree-modern-double-color-futuristic-neon-background-image_351866.jpg" +
                "&height=700" +
                "&avatar=" + member.getAvatarUrl() +
                "&username=" + name +
                "&current_exp=" + cur +
                "&max_exp=" + max +
                "&level=" + levelI +
                "&bar_color=%2300ffef";

        EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(OffsetDateTime.now()).setTitle(member.getName() + "'s Rank");
        embedBuilder.setImage(urlString);
        ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.of(ButtonStyle.SECONDARY, ctx.getAuthor().getId() + ":rank:" + member.getId(), "Show rank in another image")
        ).queue();
    }

    @Override
    public String getName() {
        return "rank";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows your rank!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
