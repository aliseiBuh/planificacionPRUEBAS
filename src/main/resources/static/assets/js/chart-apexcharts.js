// $(function () {
//     // Primero calcular, luego cargar el gráfico
//     calcularPorcentajes().then(function() {
//         cargarGrafico();
//     });
// });

// function calcularPorcentajes() {
//     return $.ajax({
//         url: '/porcentajes/calcular',
//         type: 'POST',
//         success: function(response) {
//             console.log('Cálculo completado:', response);
//         },
//         error: function(error) {
//             console.error('Error al calcular:', error);
//         }
//     });
// }

// function cargarGrafico() {
//     $.ajax({
//         url: '/porcentajes/datos-grafico',
//         type: 'GET',
//         success: function(data) {
//             console.log('Datos recibidos:', data); // Para debug
//             renderizarGrafico(data);
//         },
//         error: function(error) {
//             console.error('Error al cargar datos:', error);
//         }
//     });
// }

$(function () {
    // Cargar gráfico con datos actualizados
    cargarGraficoActualizado();
});

function cargarGraficoActualizado() {
    $.ajax({
        url: '/porcentajes/datos-grafico-actualizados',
        type: 'GET',
        success: function(data) {
            console.log('Datos actualizados recibidos:', data);
            if (data) {
                renderizarGrafico(data);
            } else {
                mostrarError("No se pudieron generar los datos del gráfico");
            }
        },
        error: function(error) {
            console.error('Error al cargar datos actualizados:', error);
            mostrarError("Error al conectar con el servidor");
        }
    });
}

function mostrarError(mensaje) {
    // Mostrar mensaje de error en lugar del gráfico
    $('#chart1').html(`
        <div class="alert alert-danger" role="alert">
            <i class="fas fa-exclamation-triangle"></i> ${mensaje}
        </div>
    `);
}

// Resto del código igual...

function renderizarGrafico(data) {
    // Preparar series dinámicamente
    var series = [];
    
    for (var areaNombre in data) {
        series.push({
            name: areaNombre,
            data: data[areaNombre]
        });
    }

    var options = {
        chart: {
            height: 350,
            type: 'bar',
        },
        plotOptions: {
            bar: {
                dataLabels: {
            position: 'top'
        },
               
            },
        },
        dataLabels: {
            enabled: true,
            formatter: function (val) {
                return val.toFixed(1) + "%";
            },
                offsetY: -20,

            style: {
                fontSize: '10px',
                colors: ["#000000ff"],
                fontWeight: 'normal' 
            }
        },
        stroke: {
            show: true,
            width: 2,
            colors: ['transparent']
        },
        series: series,
        xaxis: {
            categories: ['Gestión 2021', 'Gestión 2022', 'Gestión 2023', 'Gestión 2024', 'Gestión 2025'],
            labels: {
                style: {
                    colors: '#9aa0ac',
                }
            }
        },
        
        yaxis: {
            title: {
                text: 'Porcentaje (%)'
            },
            labels: {
                style: {
                    color: '#9aa0ac',
                },
                formatter: function (val) {
                    return val.toFixed(2) + "%";
                }
            },
            max: 100
        },
        fill: {
            opacity: 1
        },
        tooltip: {
            y: {
                formatter: function (val) {
                    return val.toFixed(2) + "%";
                }
            }
        }
    };

    var chart = new ApexCharts(document.querySelector("#chart1"), options);
    chart.render();
}