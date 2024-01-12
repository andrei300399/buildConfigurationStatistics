//var scr1 = document.getElementById("script1");
//console.log("ddddd", scr1);
//const labels = Array.from({length: 30}, (_, i) => i + 1);
//const dataTestCount = {
//  labels: labels,
//  datasets: [{
//    label: 'Test Count',
//    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
//    borderColor: [
//      'rgba(0, 180, 33, 1)',
//    ],
//    tension: 0.1
//
//  }]
//};
//var settingsTestCount = {
//                        type: 'line',
//                        data: dataTestCount,
//                      };
var ctx = document.getElementById("successRateChart").getContext("2d");
var ctxBuild = document.getElementById("buildDurationChart").getContext("2d");
var ctxArtifactsSize = document.getElementById("artifactsSize").getContext("2d");
var ctxTimeSpentQueue = document.getElementById("timeSpentQueue").getContext("2d");
var ctxTestCount = document.getElementById("testCount").getContext("2d");


function formatLabelsDate(arrLabels, dateFormat, period) {
switch(period) {
    case 'DAY':
                 arrLabels.push(
                            dateFormat.getDate()+
                                       " "+(dateFormat.getHours())+":0"+(dateFormat.getMinutes())

                       );
    break;
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
console.log("arrLabels", arrLabels);
}



function sortOnKeys(dict, period) {

    var sorted = [];
    for(var key in dict) {
        console.log("key", key);
        sorted[sorted.length] = key;
        console.log("sorted", sorted);
    }
    sorted.sort();
    console.log("sorted2", sorted);
    var dataBuildDurationValues = [];
    var labelsB = [];
    for(var i = 0; i < sorted.length; i++) {
       dataBuildDurationValues.push(dict[sorted[i]]);
       console.log("dataBuildDurationValues", dataBuildDurationValues, dict[sorted[i]]);
       var dateFormat= new Date(parseInt(sorted[i]));
       console.log("dateFormat", dateFormat);
       //var period = document.querySelector(".period").textContent;
       console.log("period", period)
       formatLabelsDate(labelsB, dateFormat, period);
       console.log("labelsB", labelsB);
    }
    return [dataBuildDurationValues, labelsB];
}

// success rate chart settings

var successRateSelect = document.querySelector("#selectSuccess");

function createSuccessRateChart(period){
console.log("period", period)
var successRate2 = document.querySelector("#successRateData").textContent;
  console.log(successRate2);
  var obj = JSON.parse(successRate2);
  console.log("json", obj);

  var dataSuccessRateValues = [];
  var dataSuccessRateDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);


dataSuccessRateDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataSuccessRateDict: ", dataSuccessRateDict);

  dictSuccess = sortOnKeys(dataSuccessRateDict, period)[0];
  labelsSuccess = sortOnKeys(dataSuccessRateDict, period)[1];

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

  if (perfChartJsCharts["successRateChart"]) perfChartJsCharts["successRateChart"].destroy();
  perfChartJsCharts["successRateChart"] = new Chart(ctx, allPerf);

}


// test count chart settings

var testCountSelect = document.querySelector("#selectTestCount");

function createTestCountChart(period){
console.log("period", period)
var testCount2 = document.querySelector("#testCountData").textContent;
  console.log(testCount2);
  var obj = JSON.parse(testCount2);
  console.log("json", obj);

  var dataTestCountValues = [];
  var dataTestCountDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);


dataTestCountDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataTestCountDict: ", dataTestCountDict);

  dictTestCount = sortOnKeys(dataTestCountDict, period)[0];
  labelsTestCount = sortOnKeys(dataTestCountDict, period)[1];

const dataTestCount = {
  labels: labelsTestCount,
  datasets: [{
    label: 'Test Count',
    data: dictTestCount,
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
var settingsTestCount = {
                        type: 'line',
                        data: dataTestCount,
                      };

  if (perfChartJsCharts["testCountChart"]) perfChartJsCharts["testCountChart"].destroy();
  perfChartJsCharts["testCountChart"] = new Chart(ctxTestCount, settingsTestCount);

}


// build duration chart settings

var buildDurationSelect = document.querySelector("#selectBuildDuration");

function createBuildDurationChart(period){
console.log("period", period)
var buildDuration = document.querySelector("#buildDurationData").textContent;
  console.log(buildDuration);
  var obj = JSON.parse(buildDuration);
  console.log("json", obj);

  var dataBuildDurationValues = [];
  var dataBuildDurationDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataBuildDurationDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataBuildDurationDict: ", dataBuildDurationDict);

  dictBuildDuration = sortOnKeys(dataBuildDurationDict, period)[0];
  labelsBuildDuration = sortOnKeys(dataBuildDurationDict, period)[1];

const dataBuildDuration = {
  labels: labelsBuildDuration,
  datasets: [{
    label: 'Build duration',
    data: dictBuildDuration,
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
var settingsBuildDuration = {
                        type: 'line',
                        data: dataBuildDuration,
                      };
  if (perfChartJsCharts["buildDurationChart"]) perfChartJsCharts["buildDurationChart"].destroy();
  perfChartJsCharts["buildDurationChart"] = new Chart(ctxBuild, settingsBuildDuration);

}



// artifact size chart settings

var artifactSizeSelect = document.querySelector("#selectArtifactSize");

function createArtifactSizeChart(period){
console.log("period", period)
var artifactSize = document.querySelector("#artifactSizeData").textContent;
  console.log(artifactSize);
  var obj = JSON.parse(artifactSize);
  console.log("json", obj);

  var dataArtifactSizeValues = [];
  var dataArtifactSizeDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataArtifactSizeDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataArtifactSizeDict: ", dataArtifactSizeDict);

  dictArtifactSize = sortOnKeys(dataArtifactSizeDict, period)[0];
  labelsArtifactSize = sortOnKeys(dataArtifactSizeDict, period)[1];

const dataArtifactSize = {
  labels: labelsArtifactSize,
  datasets: [{
    label: 'Artifact Size',
    data: dictArtifactSize,
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
var settingsArtifactSize = {
                        type: 'line',
                        data: dataArtifactSize,
                      };
  if (perfChartJsCharts["artifactSizeChart"]) perfChartJsCharts["artifactSizeChart"].destroy();
  perfChartJsCharts["artifactSizeChart"] = new Chart(ctxArtifactsSize, settingsArtifactSize);

}

// time queue chart settings

var timeQueueSelect = document.querySelector("#selectTimeQueue");

function createTimeQueueChart(period){
console.log("period", period)
var timeQueue = document.querySelector("#timeQueueData").textContent;
  console.log(timeQueue);
  var obj = JSON.parse(timeQueue);
  console.log("json", obj);

  var dataTimeQueueValues = [];
  var dataTimeQueueDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataTimeQueueDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataTimeQueueDict: ", dataTimeQueueDict);

  dictTimeQueue = sortOnKeys(dataTimeQueueDict, period)[0];
  labelsTimeQueue = sortOnKeys(dataTimeQueueDict, period)[1];

const dataTimeQueue = {
  labels: labelsTimeQueue,
  datasets: [{
    label: 'Time Spent In Queue',
    data: dictTimeQueue,
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
var settingsTimeQueue = {
                        type: 'line',
                        data: dataTimeQueue,
                      };
  if (perfChartJsCharts["timeQueueChart"]) perfChartJsCharts["timeQueueChart"].destroy();
  perfChartJsCharts["timeQueueChart"] = new Chart(ctxTimeSpentQueue, settingsTimeQueue);

}



