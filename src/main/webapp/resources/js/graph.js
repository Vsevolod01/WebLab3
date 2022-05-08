const SVG_SIZE = 300;
const CANVAS_R_VALUE = 100;
const DEFAULT_R_VALUE = 1;

// Функции для вычисления Y и X

function fromTableToSvgX(x, r) {
    return x / r * CANVAS_R_VALUE + SVG_SIZE / 2;
}

function fromTableToSvgY(y, r) {
    return SVG_SIZE / 2 - y / r * CANVAS_R_VALUE;
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
    $("tbody tr").each(function () {
        let point = $(this);

        let x = parseFloat(point.find("td:first-child").text());
        let y = parseFloat(point.find("td:nth-child(2)").text());
        let r = parseFloat(point.find("td:nth-child(3)").text());
        let result = point.find("td:nth-child(4)").text()
        if (isNaN(x) || isNaN(y)) return;

        let color = (result == "true")? 'green' : 'red';
        if (r !== getRValue()) {
            color = 'indigo';
        }
        console.log(getRValue())
        let plot = $(".svg-container svg");

        let existingContent = plot.html();
        let contentToInsert = `<circle class="dot" 
                                         r="4" 
                                         cx="${fromTableToSvgX(x, r)}" 
                                         cy="${fromTableToSvgY(y, r)}" 
                                         fill="${color}"/>`;
        plot.html(existingContent + contentToInsert);
    })
}

function clearTable() {
    $("tbody tr").remove();
}

// Удаляет точки с графика
function clearPlot() {
    $(".dot").remove();
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

function myFunc(data) {
    if (data.status == "success") {
        // drawDot();
        drawDotsFromTable()
    }
}