package com.sparta.gitandrun.user.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.sparta.gitandrun.user.entity.QUser.user;

/*
 * Q타입은 Querydsl이 컴파일 시점에 자동으로 생성한 클래스.
 * User 엔티티를 기반으로 생성되며, 타입 세이프하게 쿼리를 생섬함.
 * static import된 QUser.user는 User 엔티티의 싱글톤 인스턴스임
 */

@Slf4j
@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {


    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Page<User> searchUsers(String username, String email, String phone, String nickName, Role role, Pageable pageable) {
        log.info("Repository - username: {}", username);

        // from(user)로 QUser 엔티티로부터 쿼리 시작
        // QUser를 통해 User 엔티티의 필드들을 타입 안전하게 접근 가능
        // 예: user.username, user.email 등으로 컴파일 시점에 오류 체크 가능
        // SELECT * FROM p_user WHERE
        JPQLQuery<User> query = from(user)
                .where(
                        containsUsername(username), //username ILIKE '%검색어%'
                        containsEmail(email),   // AND email ILIKE '%검색어%'
                        containsPhone(phone),  // AND phone LIKE '%검색어%'
                        containsNickName(nickName), // AND nick_name ILIKE '%검색어%' .. 이하 생략
                        eqRole(role),
                        user.isDeleted.eq(false)
                );

        /*
         * 페이징 처리가 적용된 최종 SQL:
         * ... ORDER BY created_at LIMIT 10 OFFSET 0
         */
        List<User> users = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(users, pageable, query.fetchCount());
    }


    /* BooleanExpression은 Querydsl의 조건식을 표현하는 클래스
     * containsUsername("myname")는
     * WHERE username ILIKE '%myname%'
     * 아래 메서드도 동일한 로직
     */
    private BooleanExpression containsUsername(String username) {
        // username 파라미터가 없으면 null 반환 -> 해당 조건은 무시됨
        return StringUtils.hasText(username) ? user.username.containsIgnoreCase(username) : null;
    }

    private BooleanExpression containsEmail(String email) {
        return StringUtils.hasText(email) ? user.email.containsIgnoreCase(email) : null;
    }

    private BooleanExpression containsPhone(String phone) {
        return StringUtils.hasText(phone) ? user.phone.contains(phone) : null;
    }

    private BooleanExpression containsNickName(String nickName) {
        return StringUtils.hasText(nickName) ? user.nickName.containsIgnoreCase(nickName) : null;
    }

    private BooleanExpression eqRole(Role role) {
        return role != null ? user.role.eq(role) : null;
    }

}