$('.button-input').click(function () {
    $('.button-input-selected')
        .add(this)
        .toggleClass('button-input-selected')
        .toggleClass('button-input');
})

// Проверка на попадание точки на график
function checkResult(x, y, r) {
    return  (x >= 0 && y >= 0 && y <= -x + r) || //треугольник
        //прямоугольник
        (x <= 0 && y >= 0 && x >= -r / 2 && y <= r) ||
        //четверть круга
        (x <= 0 && y <= 0 && x * x + y * y <= (r / 2) * (r / 2));
}

// Получаем значение R
function getRValue() {
    let rValue = parseFloat($("input[name=\"newEntryForm:R_field_input\"]").attr("aria-valuenow"));
    if (isNaN(rValue)) {
        rValue = parseFloat($("tbody tr").last().find(">:nth-child(3)").text());
        if (isNaN(rValue)) {
            rValue = DEFAULT_R_VALUE;
        }
    }
    return rValue;
}

function fixYField() {
    let yField = $("input[name=\"newEntryForm:Y_field\"]")
    yField.removeAttr("type")
    yField.attr("type", "number")
}

// При нажатии на график выполнить функцию из graph.js
$(".svg-container svg").click(clickPlotHandler);

// Перерисовка графика по изменению R
$("input[name=\"newEntryForm:R_field_input\"]").change(function () {
    deleteAllPointsFromPlot();
    drawDotsFromTable();
})

// Выполняемые функции при загрузке
fixYField();
drawDotsFromTable();
