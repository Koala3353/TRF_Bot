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
        String background = "https://media.discordapp.net/attachments/877423237200510996/918754304570052608/image0.png";
        long max = LevelPointManager.calculateLevelMax(levelI);
        long cur = GetData.getLevelPoints(member);
        String urlString = "https://api.willz.repl.co/image/rankcard?username=" +
                name + "&discriminator=" + member.getDiscriminator() + "&level=" + levelI + "&xp=" + cur + "&req=" + max + "&rank=999&status=" + ctx.getMember().getOnlineStatus().name().toLowerCase() + "&image=" + member.getAvatarUrl() + "&background=" + background;

        EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(OffsetDateTime.now()).setTitle(member.getName() + "'s Rank");
        embedBuilder.setImage(urlString);
        embedBuilder.setFooter("Kindly ignore the rank in the image");
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
