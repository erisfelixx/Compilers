#include "evaluator.hh"
#include "../utils/errors.hh"

namespace ast {

//база - просто листя
int32_t Evaluator::visit(const IntegerLiteral &node) {
    return node.value;
}

// спочатку рахуємо що зліва-справа, потім застосуваємо оператор
int32_t Evaluator::visit(const BinaryOperator &node) {
    int32_t left = node.get_left().accept(*this);
    int32_t right = node.get_right().accept(*this);

    switch (node.op) {
        case o_plus:   return left + right;
        case o_minus:  return left - right;
        case o_times:  return left * right;
        case o_divide:
            if (right == 0) {
                utils::error(node.loc, "division by zero");
                return 0; 
            }
            return left / right;
        case o_eq:     return left == right;
        case o_neq:    return left != right;
        case o_lt:     return left < right;
        case o_le:     return left <= right;
        case o_gt:     return left > right;
        case o_ge:     return left >= right;
        default:       
            utils::error(node.loc, "unknown operator"); 
            return 0;
    }
}

int32_t Evaluator::visit(const Sequence &node) {
    if (node.get_exprs().empty()) {
        utils::error(node.loc, "empty sequence");
        return 0;
    }
    
    int32_t result = 0;
    for (auto expr : node.get_exprs()) {
        result = expr->accept(*this);
    }
    return result; // Повертає значення останнього виразу
}

// те саме "ліниве обчислення" - рахуємо лише умову
int32_t Evaluator::visit(const IfThenElse &node) {
    // Для логічних виразів 0 - це false, все інше - true
    if (node.get_condition().accept(*this) != 0) {
        return node.get_then_part().accept(*this);
    } else {
        return node.get_else_part().accept(*this);
    }
}


// --- Вузли, які не підтримуються калькулятором (генерують помилку) ---

int32_t Evaluator::visit(const StringLiteral &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const Let &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const Identifier &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const VarDecl &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const FunDecl &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const FunCall &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const WhileLoop &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const ForLoop &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const Break &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

int32_t Evaluator::visit(const Assign &node) {
    utils::error(node.loc, "not evaluable");
    return 0;
}

} // namespace ast