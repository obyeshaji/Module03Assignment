package edu.farmingdale.module3assignment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HelloController {

    @FXML
    private Button findSolutionButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button verifyButton;

    // Top solution field (non-editable)
    @FXML
    private TextField solutionField;

    // Bottom user input field (editable)
    @FXML
    private TextField userExpressionField;

    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;

    // Store the 4 current card indices (1..52) and their face values (1..13)
    private final int[] currentCardIndices = new int[4];
    private final int[] currentCardValues = new int[4];

    private final Random random = new Random();

    @FXML
    public void initialize() {
        refreshCards();
    }

    @FXML
    public void onRefreshButtonClicked() {
        refreshCards();
    }

    @FXML
    public void onFindSolutionClicked() {
        String solution = findSolution(currentCardValues);
        if (solution != null) {
            // Display the auto-generated solution in the non-editable field
            solutionField.setText(solution);
        } else {
            showAlert("No solution", "No solution found for these cards.");
        }
    }

    @FXML
    public void onVerifyButtonClicked() {
        // Read from the user input field (bottom text field)
        String userExpression = userExpressionField.getText().trim();
        if (userExpression.isEmpty()) {
            showAlert("Error", "Please enter an expression.");
            return;
        }

        // Verify that the expression uses the four card values exactly once
        if (!usesAllFourValues(userExpression, currentCardValues)) {
            showAlert("Result", "You must use exactly the 4 displayed card values once.");
            return;
        }

        try {
            double result = evaluateExpression(userExpression);
            if (Math.abs(result - 24.0) < 1e-6) {
                showAlert("Result", "Congratulations! The expression evaluates to 24.");
            } else {
                showAlert("Result", "Sorry, the expression does not evaluate to 24. (Got " + result + ")");
            }
        } catch (Exception e) {
            showAlert("Error", "Invalid expression or evaluation error:\n" + e.getMessage());
        }
    }

    private void refreshCards() {
        for (int i = 0; i < 4; i++) {
            currentCardIndices[i] = random.nextInt(52) + 1;
            currentCardValues[i] = getCardValue(currentCardIndices[i]);
        }
        updateCardImages();
        solutionField.clear();
        userExpressionField.clear();
    }

    private void updateCardImages() {
        card1.setImage(new Image(getClass().getResourceAsStream(
                "/edu/farmingdale/module3assignment/cards/" + getCardFileName(currentCardIndices[0]))
        ));
        card2.setImage(new Image(getClass().getResourceAsStream(
                "/edu/farmingdale/module3assignment/cards/" + getCardFileName(currentCardIndices[1]))
        ));
        card3.setImage(new Image(getClass().getResourceAsStream(
                "/edu/farmingdale/module3assignment/cards/" + getCardFileName(currentCardIndices[2]))
        ));
        card4.setImage(new Image(getClass().getResourceAsStream(
                "/edu/farmingdale/module3assignment/cards/" + getCardFileName(currentCardIndices[3]))
        ));
    }

    private String getCardFileName(int index) {
        int rank = (index - 1) % 13 + 1;
        int suitIndex = (index - 1) / 13;
        String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        return ranks[rank - 1] + "_of_" + suits[suitIndex] + ".png";
    }

    private int getCardValue(int index) {
        return (index - 1) % 13 + 1;
    }

    private boolean usesAllFourValues(String expression, int[] cardValues) {
        List<Integer> found = new ArrayList<>();
        String[] tokens = expression.split("[^0-9]+");
        for (String t : tokens) {
            if (!t.isEmpty()) {
                found.add(Integer.parseInt(t));
            }
        }
        if (found.size() != 4) {
            return false;
        }
        Collections.sort(found);
        List<Integer> cardSorted = new ArrayList<>();
        for (int v : cardValues) {
            cardSorted.add(v);
        }
        Collections.sort(cardSorted);
        return found.equals(cardSorted);
    }

    private double evaluateExpression(String expression) {
        return new ExpressionParser(expression).parse();
    }

    private String findSolution(int[] values) {
        List<Pair> list = new ArrayList<>();
        for (int v : values) {
            list.add(new Pair(v, String.valueOf(v)));
        }
        return search(list);
    }

    private String search(List<Pair> pairs) {
        final double EPSILON = 1e-6;
        if (pairs.size() == 1) {
            if (Math.abs(pairs.get(0).value - 24.0) < EPSILON) {
                return pairs.get(0).expr;
            }
            return null;
        }
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = i + 1; j < pairs.size(); j++) {
                Pair a = pairs.get(i);
                Pair b = pairs.get(j);
                List<Pair> next = new ArrayList<>(pairs);
                next.remove(j);
                next.remove(i);
                next.add(new Pair(a.value + b.value, "(" + a.expr + "+" + b.expr + ")"));
                String res = search(next);
                if (res != null) return res;
                next.remove(next.size() - 1);
                next.add(new Pair(a.value - b.value, "(" + a.expr + "-" + b.expr + ")"));
                res = search(next);
                if (res != null) return res;
                next.remove(next.size() - 1);
                next.add(new Pair(b.value - a.value, "(" + b.expr + "-" + a.expr + ")"));
                res = search(next);
                if (res != null) return res;
                next.remove(next.size() - 1);
                next.add(new Pair(a.value * b.value, "(" + a.expr + "*" + b.expr + ")"));
                res = search(next);
                if (res != null) return res;
                next.remove(next.size() - 1);
                if (Math.abs(b.value) > EPSILON) {
                    next.add(new Pair(a.value / b.value, "(" + a.expr + "/" + b.expr + ")"));
                    res = search(next);
                    if (res != null) return res;
                    next.remove(next.size() - 1);
                }
                if (Math.abs(a.value) > EPSILON) {
                    next.add(new Pair(b.value / a.value, "(" + b.expr + "/" + a.expr + ")"));
                    res = search(next);
                    if (res != null) return res;
                    next.remove(next.size() - 1);
                }
            }
        }
        return null;
    }

    private static class Pair {
        double value;
        String expr;
        Pair(double value, String expr) {
            this.value = value;
            this.expr = expr;
        }
    }

    private static class ExpressionParser {
        private final String str;
        private int pos = -1, ch;
        ExpressionParser(String str) {
            this.str = str;
            nextChar();
        }
        private void nextChar() {
            pos++;
            ch = pos < str.length() ? str.charAt(pos) : -1;
        }
        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) { nextChar(); return true; }
            return false;
        }
        double parse() {
            double x = parseExpression();
            if (pos < str.length()) {
                throw new RuntimeException("Unexpected character: " + (char)ch);
            }
            return x;
        }
        private double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if (eat('+')) x += parseTerm();
                else if (eat('-')) x -= parseTerm();
                else return x;
            }
        }
        private double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if (eat('*')) x *= parseFactor();
                else if (eat('/')) x /= parseFactor();
                else return x;
            }
        }
        private double parseFactor() {
            if (eat('+')) return parseFactor();
            if (eat('-')) return -parseFactor();
            double x;
            int startPos = this.pos;
            if (eat('(')) {
                x = parseExpression();
                if (!eat(')')) throw new RuntimeException("Missing closing parenthesis");
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else {
                throw new RuntimeException("Unexpected character: " + (char)ch);
            }
            return x;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}