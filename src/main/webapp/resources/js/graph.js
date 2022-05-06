const SVG_SIZE = 300;
const CANVAS_R_VALUE = 100;
const DEFAULT_R_VALUE = 1;

// Функции для вычисления Y и X

function fromTableToSvgX(x) {
    return x / getRValue() * CANVAS_R_VALUE + SVG_SIZE / 2;
}

function fromTableToSvgY(y) {
    return SVG_SIZE / 2 - y / getRValue() * CANVAS_R_VALUE;
}

function fromSvgToRX(x) {
    return getRValue() * (x - SVG_SIZE / 2) / CANVAS_R_VALUE;
}

function fromSvgToRY(y) {
    return getRValue() * (SVG_SIZE / 2 - y) / CANVAS_R_VALUE;
}


// Рисует с таблицы точки
function drawDotsFromTable() {
    clearPlot();
    let count = 0;
    $("tbody tr").each(function () {
        let point = $(this);

        let x = parseFloat(point.find("td:first-child").text());
        let y = parseFloat(point.find("td:nth-child(2)").text());

        if (isNaN(x) || isNaN(y)) return;

        let color = checkResult(x, y, getRValue()) ? 'green' : 'red';

        let plot = $(".svg-container svg");

        let existingContent = plot.html();
        let contentToInsert = `<circle class="dot" 
                                         r="4" 
                                         cx="${fromTableToSvgX(x)}" 
                                         cy="${fromTableToSvgY(y)}" 
                                         fill="${color}"/>`;
        plot.html(existingContent + contentToInsert);
        count++;
    })
    console.log(count);

}

function clearTable() {
    $("tbody tr").remove();
}

// Удаляет точки с графика
function clearPlot() {
    $(".dot").remove();
}

function drawDot() {
    let x = $('.pointX').val();
    let y = $('.pointY').val();
    let color = checkResult(x, y, getRValue()) ? 'green' : 'red';

    let plot = $(".svg-container svg");
    let existingContent = plot.html();
    let contentToInsert = `<circle class="dot" 
                                         r="4" 
                                         cx="${fromTableToSvgX(x)}" 
                                         cy="${fromTableToSvgY(y)}" 
                                         fill="${color}"/>`;
    plot.html(existingContent + contentToInsert);
}

// Нажатие на график и отправка формы
function clickPlotHandler(e) {
    const offset = $(this).offset();
    const x = e.pageX - offset.left;
    const y = e.pageY - offset.top;

    let xValue = fromSvgToRX(x);
    let yValue = fromSvgToRY(y);
    const rValue = getRValue();

    $(".pointX").val(xValue.toFixed(2));
    $(".pointY").val(yValue.toFixed(2));
    $(".pointR").val(rValue);
    $(".pointResult").val(checkResult(xValue, yValue, rValue));
    $(".submitSvg").click();

    drawDotsFromTable();
}