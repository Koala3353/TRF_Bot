package com.general_hello.bot.objects;

import com.general_hello.bot.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HierarchicalRoles {
    private List<Long> requiredRoles;
    private long role;

    public HierarchicalRoles(long role) {
        this.role = role;
        this.requiredRoles = new ArrayList<>();
    }

    public List<Long> getRequiredRoles() {
        return requiredRoles;
    }

    public HierarchicalRoles addRequiredRole(long role) {
        this.requiredRoles.add(role);
        return this;
    }

    public void save() {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Roles" +
                        "(RoleId, RequiredRoles)" +
                        "VALUES (?, ?);")) {
            preparedStatement.setString(1, String.valueOf(getRole()));
            preparedStatement.setString(2, requiredRoles());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> hierarchicalRoles() {
        List<Long> roles = new ArrayList<>();
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RoleId FROM Roles")) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(resultSet.getLong("RoleId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public static List<Long> getRequiredRolesForRole(long roleIdToQuery) {
        String roles = "";
        List<Long> requiredRoles = new ArrayList<>();
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RequiredRoles FROM Roles WHERE RoleId = ?")) {

            preparedStatement.setString(1, String.valueOf(roleIdToQuery));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    roles = resultSet.getString("RequiredRoles");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String roleReq : roles.split(",")) {
            if (roleReq.equals("")) {
                continue;
            }
            requiredRoles.add(Long.valueOf(roleReq));
        }

        return requiredRoles;
    }

    public HierarchicalRoles setRequiredRoles(List<Long> requiredRoles) {
        this.requiredRoles = requiredRoles;
        return this;
    }

    public String requiredRoles() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Long role : requiredRoles) {
            stringBuilder.append(role).append(",");
        }
        return stringBuilder.toString();
    }

    public long getRole() {
        return role;
    }

    public HierarchicalRoles setRole(long role) {
        this.role = role;
        return this;
    }
}
