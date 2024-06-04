package com.vocahype.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class PartitionLearningsRepositoryCustomImpl {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createLearningPartition(final String schema, final String parentTable, final int users) {
        String partitionTableName = parentTable + "_" + users / 1000;
        if (!checkIfTableExists(schema, partitionTableName) && checkIfTableExists(schema, parentTable)) {
            createLike(schema, partitionTableName, parentTable);
            inherit(schema, partitionTableName, parentTable);
            addFK(schema, partitionTableName, "word_id", "vh", "words", "id");
            addFK(schema, partitionTableName, "word_comprehension_levels_id", "vh", "word_comprehension_levels", "id");
            addUsersRangeCheck(schema, partitionTableName, users);
//            grantAll(schema, partitionTableName, "its"); // root user
//            changeOwner(schema, partitionTableName, "its"); // root user
//            grantAll(schema, partitionTableName, "api"); // this user
        }
    }

//    private void changeOwner(final String schemaName, final String tableName, final String user) {
//        String sql = String.format("ALTER TABLE %s OWNER TO %s;", getTable(schemaName, tableName), user);
//        jdbcTemplate.execute(sql);
//    }
//
//    private void grantAll(final String schemaName, final String tableName, final String user) {
//        String sql = String.format("GRANT ALL ON TABLE %s TO %s;", getTable(schemaName, tableName), user);
//        jdbcTemplate.execute(sql);
//    }

    private void addUsersRangeCheck(final String schemaName, final String tableName, final int users) {
        String sql = String.format("ALTER TABLE %s " // table
                        + "ADD CONSTRAINT %s_user_range_check "
                        + "CHECK ((\"user_id\" >= %s "
                        + "AND \"user_id\" < %s));",
                getTable(schemaName, tableName),
                getTable(schemaName, tableName).replace(".", "_"),
                users,
                users + 1000
        );
        jdbcTemplate.execute(sql);
    }

    private void addFK(final String schemaName, final String tableName, final String field, final String refSchemaName,
                       final String refTableName, final String refField) {
        String sql = String.format("ALTER TABLE %s ADD CONSTRAINT %s_%s_%s_%s_fk FOREIGN KEY (%s) REFERENCES %s (%s) "
                        + "MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;",
                getTable(schemaName, tableName),
                getTable(schemaName, tableName).replace(".", "_"),
                field,
                getTable(refSchemaName, refTableName).replace(".", "_"),
                refField,
                field,
                getTable(refSchemaName, refTableName),
                refField
        );
        jdbcTemplate.execute(sql);
    }

    private void inherit(final String schemaName, final String partitionTableName, final String parentTableName) {
        String sql = String.format("ALTER TABLE %s INHERIT %s",
                getTable(schemaName, partitionTableName),
                getTable(schemaName, parentTableName));
        jdbcTemplate.execute(sql);
    }

    private void createLike(final String schemaName, final String partitionTableName, final String parentTableName) {
        String sql = String.format("CREATE TABLE %s (LIKE %s INCLUDING ALL)",
                getTable(schemaName, partitionTableName),
                getTable(schemaName, parentTableName)
        );
        jdbcTemplate.execute(sql);
    }

    public static String getTable(final String schema, final String table) {
        return String.format("%s.%s", schema, table);
    }

    public boolean checkIfTableExists(final String schemaName, final String tableName) {
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = ? AND table_name = ?);";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, schemaName, tableName));
    }
}
