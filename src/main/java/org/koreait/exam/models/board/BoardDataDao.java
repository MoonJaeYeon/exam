package org.koreait.exam.models.board;

import lombok.RequiredArgsConstructor;
import org.koreait.exam.controllers.board.BoardDataForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardDataDao {
    private final JdbcTemplate jdbcTemplate;

    public boolean save(BoardDataForm data) {
        if (data.getId() > 0) {
            return updateBoardData(data);
        } else {
            return insertBoardData(data);
        }
    }

    private boolean updateBoardData(BoardDataForm data) {
        String sql = "UPDATE BOARD_DATA " +
                " SET " +
                " POSTER = ?, " +
                " SUBJECT = ?, " +
                " CONTENT = ?, " +
                " MODDT = SYSDATE " +
                " WHERE ID = ?";

        int affectedRows = jdbcTemplate.update(
                sql, data.getPoster(), data.getSubject(), data.getContent(), data.getId()
        );

        return affectedRows > 0;
    }

    private boolean insertBoardData(BoardDataForm data) {
        String sql = "INSERT INTO BOARD_DATA (ID, POSTER, SUBJECT, CONTENT) " +
                " VALUES (BOARD_DATA_SEQ.nextval, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int affectedRows = jdbcTemplate.update(
                con -> {
                    PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"ID"});
                    pstmt.setString(1, data.getPoster());
                    pstmt.setString(2, data.getSubject());
                    pstmt.setString(3, data.getContent());
                    return pstmt;
                },
                keyHolder
        );

        long id = keyHolder.getKey().longValue();
        data.setId(id);

        return affectedRows > 0;
    }

    public BoardData get(long id) {
        try {
            String sql = "SELECT * FROM BOARD_DATA WHERE ID = ?";
            BoardData data = jdbcTemplate.queryForObject(sql, this::mapper, id);

            return data;
        } catch (Exception e) {
            return null;
        }
    }
    public List<BoardData> getAll() {
        String sql = "SELECT * FROM TEST_BOARD";
        return jdbcTemplate.query(sql, this::mapper);
    }
    public BoardData mapper(ResultSet rs, int i) throws SQLException {
        Timestamp modDt = rs.getTimestamp("MODDT");
        return BoardData.builder()
                .id(rs.getLong("ID"))
                .poster(rs.getString("POSTER"))
                .subject(rs.getString("SUBJECT"))
                .content(rs.getString("CONTENT"))
                .regDt(rs.getTimestamp("REGDT").toLocalDateTime())
                .modDt(modDt == null ? null : modDt.toLocalDateTime())
                .build();
    }
}
