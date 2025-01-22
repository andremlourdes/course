package com.ead.course.repositories;

import org.hibernate.sql.ast.tree.expression.JdbcParameter;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {
}
