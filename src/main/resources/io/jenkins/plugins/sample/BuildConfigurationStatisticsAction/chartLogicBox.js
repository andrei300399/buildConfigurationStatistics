var scr1 = document.getElementById("script1");
console.log("ddddd", scr1);


function formatLabelsDate(arrLabels, dateFormat, period) {
switch(period) {
    case 'WEEK':
    case 'MONTH':
                 arrLabels.push(
                            dateFormat.getDate()+
                                       "/"+(dateFormat.getMonth()+1)+
                                       "/"+dateFormat.getFullYear()
                       );
    break;

    case 'QUARTER':
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
// build Duration create chart settings
var buildDuration = document.querySelectorAll(".buildDuration");

var dataBuildDurationValues = [];
var dataBuildDurationDict = {};

for (var i=0; i<buildDuration.length; i++){

    dataBuildDurationDict[Date.parse(buildDuration[i].querySelector('.key').textContent)]
        = parseFloat(buildDuration[i].querySelector('.value').textContent);

}
console.log("dataBuildDurationDict: ", dataBuildDurationDict);

dict = sortOnKeys(dataBuildDurationDict)[0];
labelsB = sortOnKeys(dataBuildDurationDict)[1];
console.log("build duration dict values",dict);

// build Duration create chart settings
var successRate = document.querySelectorAll(".successRate");

var dataSuccessRateValues = [];
var dataSuccessRateDict = {};

for (var i=0; i<successRate.length; i++){

    dataSuccessRateDict[Date.parse(successRate[i].querySelector('.key').textContent)]
        = parseFloat(successRate[i].querySelector('.value').textContent);

}
console.log("dataSuccessRateDict: ", dataSuccessRateDict);

dictSuccess = sortOnKeys(dataSuccessRateDict)[0];
labelsSuccess = sortOnKeys(dataSuccessRateDict)[1];
console.log("success rate dict values",dictSuccess);


// time Queue create chart settings
var timeQueue = document.querySelectorAll(".timeQueue");

var dataTimeQueueValues = [];
var dataTimeQueueDict = {};

for (var i=0; i<timeQueue.length; i++){

    dataTimeQueueDict[Date.parse(timeQueue[i].querySelector('.key').textContent)]
        = parseFloat(timeQueue[i].querySelector('.value').textContent);

}
console.log("dataTimeQueueDict: ", dataTimeQueueDict);

dictTimeQueue = sortOnKeys(dataTimeQueueDict)[0];
labelsTimeQueue = sortOnKeys(dataTimeQueueDict)[1];
console.log("time spent queue dict values",dictTimeQueue);

// artifact create chart settings
var artifactSize = document.querySelectorAll(".artifactSize");

var dataArtifactSizeValues = [];
var dataArtifactSizeDict = {};

for (var i=0; i<artifactSize.length; i++){

    dataArtifactSizeDict[Date.parse(artifactSize[i].querySelector('.key').textContent)]
        = parseFloat(artifactSize[i].querySelector('.value').textContent);

}
console.log("dataArtifactSizeDict: ", dataArtifactSizeDict);

dictArtifactSize = sortOnKeys(dataArtifactSizeDict)[0];
labelsArtifactSize = sortOnKeys(dataArtifactSizeDict)[1];
console.log("artifact size dict values",dictArtifactSize);




const labels = Array.from({length: 30}, (_, i) => i + 1);

const data = {
  labels: labelsSuccess,
  datasets: [{
    label: 'Success rate',
    data: dictSuccess,
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
  labels: labelsB,
  datasets: [{
    label: 'Time Spent In Queue',
    data: dictTimeQueue,
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
  labels: labelsArtifactSize,
  datasets: [{
    label: 'Artifacts Size',
    data: dictArtifactSize,
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








