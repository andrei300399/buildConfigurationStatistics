
const labels = Array.from({length: 30}, (_, i) => i + 1);
const data = {
  labels: labels,
  datasets: [{
    label: 'Success rate',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 100)),
    backgroundColor: [
      'rgba(0, 255, 0, 0.5)',
    ],
    borderColor: [
      'rgb(0, 69, 36)',
    ],
    categoryPercentage: 1,
    borderWidth: 1,
    barPercentage: 1,
  }]
};

function formatLabelsDate(arrLabels, dateFormat, period) {
switch(period) {
    case 'MONTH':
                 arrLabels.push(
                            dateFormat.getDate()+
                                       "/"+(dateFormat.getMonth()+1)+
                                       "/"+dateFormat.getFullYear()
                       );
    break;

    case 'YEAR':
                 arrLabels.push(
(dateFormat.getMonth()+1)+"/"+dateFormat.getFullYear()
                       );
    break;

}
}



function sortOnKeys(dict) {

    var sorted = [];
    for(var key in dict) {
        sorted[sorted.length] = key;
    }
    sorted.sort();

    var dataBuildDurationValues = [];
    var labelsB = [];
    for(var i = 0; i < sorted.length; i++) {
       dataBuildDurationValues.push(dict[sorted[i]]);
       var dateFormat= new Date(parseInt(sorted[i]));
       console.log("dateFormat", dateFormat);
       var period = document.querySelector(".period").textContent;
       formatLabelsDate(labelsB, dateFormat, period);
    }
    return [dataBuildDurationValues, labelsB];
}

var buildDuration = document.querySelectorAll(".buildDuration");

var dataBuildDurationValues = [];//Array.from({length: buildDuration.}, () => 0);
var dataBuildDurationDict = {};//Array.from({length: buildDuration.}, () => 0);

for (var i=0; i<buildDuration.length; i++){

    dataBuildDurationDict[Date.parse(buildDuration[i].querySelector('.key').textContent)]
        = parseFloat(buildDuration[i].querySelector('.value').textContent);

}
console.log("dataBuildDurationDict: ", dataBuildDurationDict);

dict = sortOnKeys(dataBuildDurationDict)[0];
labelsB = sortOnKeys(dataBuildDurationDict)[1];
console.log("asd",dict);



const dataBuildDuration = {
  labels: labelsB,
  datasets: [{
    label: 'Build duration',
    data: dict,
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};

const dataTimeSpentQueue = {
  labels: labels,
  datasets: [{
    label: 'Time Spent In Queue',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
const dataTestCount = {
  labels: labels,
  datasets: [{
    label: 'Test Count',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
const dataArtifactsSize = {
  labels: labels,
  datasets: [{
    label: 'Artifacts Size',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};








var allPerf = {
                        type: 'bar',
                        data: data,
                        options: {
                            scales: {
                                  y: {
                                    beginAtZero: true
                                  }
                                }
                        }
                      };


var settingsBuildDuration = {
                        type: 'line',
                        data: dataBuildDuration,
                      };
var settingsTimeSpentQueue = {
                        type: 'line',
                        data: dataTimeSpentQueue,
                      };
var settingsTestCount = {
                        type: 'line',
                        data: dataTestCount,
                      };
var settingsArtifactsSize = {
                        type: 'line',
                        data: dataArtifactsSize,
                      };
var ctx = document.getElementById("successRateChart").getContext("2d");
var ctxBuild = document.getElementById("buildDurationChart").getContext("2d");
var ctxTimeSpentQueue = document.getElementById("timeSpentQueue").getContext("2d");
var ctxTestCount = document.getElementById("testCount").getContext("2d");
var ctxArtifactsSize = document.getElementById("artifactsSize").getContext("2d");
perfChartJsCharts["successRateChart"] = new Chart(ctx, allPerf);
perfChartJsCharts["buildDurationChart"] = new Chart(ctxBuild, settingsBuildDuration);
perfChartJsCharts["timeSpentQueue"] = new Chart(ctxTimeSpentQueue, settingsTimeSpentQueue);
perfChartJsCharts["testCount"] = new Chart(ctxTestCount, settingsTestCount);
perfChartJsCharts["artifactsSize"] = new Chart(ctxArtifactsSize, settingsArtifactsSize);




