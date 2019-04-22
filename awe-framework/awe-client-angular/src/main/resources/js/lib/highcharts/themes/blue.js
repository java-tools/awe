/**
 * Blue theme for Highcharts JS
 * @author Pablo Vidal
 */

Highcharts.theme["blue"] = {
  colors: [ "#DDDF0D", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee" ],
  chart: {
    backgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 1,
        y2: 1
      },
      stops: [ [ 0, 'rgb(113, 113, 213)' ], [ 1, 'rgb(48, 48, 96)' ] ]
    },
    borderColor: '#C5C5E9',
    borderWidth: 2,
    className: 'dark-container',
    plotBackgroundColor: 'rgba(255, 255, 255, .1)',
    plotBorderColor: 'white',
    plotBorderWidth: 1,
    selectionMarkerFill: 'rgba(40, 40, 95, 0.5)'
  },
  title: {
    style: {
      color: '#DBDBF1',
      font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
    }
  },
  subtitle: {
    style: {
      color: '#D5D5EB',
      font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
    }
  },
  xAxis: {
    gridLineColor: '#7171A0',
    gridLineWidth: 1,
    labels: {
      style: {
        color: '#F8F8FC'
      }
    },
    lineColor: '#C6C6E3',
    tickColor: '#C6C6E3',
    title: {
      style: {
        color: 'white',
        fontWeight: 'bold',
        fontSize: '12px',
        fontFamily: 'Trebuchet MS, Verdana, sans-serif'

      }
    }
  },
  yAxis: {
    gridLineColor: '#7171A0',
    labels: {
      style: {
        color: '#F8F8FC'
      }
    },
    lineColor: '#C6C6E3',
    minorTickInterval: null,
    tickColor: '#C6C6E3',
    tickWidth: 1,
    title: {
      style: {
        color: 'white',
        fontWeight: 'bold',
        fontSize: '12px',
        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
      }
    }
  },
  tooltip: {
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    style: {
      color: '#F0F0F0'
    }
  },
  toolbar: {
    itemStyle: {
      color: '#F8F8FC'
    }
  },
  plotOptions: {
    line: {
      dataLabels: {
        color: 'white'
      },
      marker: {
        lineColor: '#F8F8FC'
      }
    },
    spline: {
      marker: {
        lineColor: '#F8F8FC'
      }
    },
    scatter: {
      marker: {
        lineColor: '#F8F8FC'
      }
    },
    candlestick: {
      lineColor: 'white'
    }
  },
  legend: {
    itemStyle: {
      font: '9pt Trebuchet MS, Verdana, sans-serif',
      color: '#FFF'
    },
    itemHoverStyle: {
      color: '#4BDD1F'
    },
    itemHiddenStyle: {
      color: '#A0A0A0'
    }
  },
  credits: {
    style: {
      color: '#666'
    }
  },
  labels: {
    style: {
      color: 'white'
    }
  },

  navigation: {
    buttonOptions: {
      symbolStroke: '#DDDDDD',
      hoverSymbolStroke: '#FFFFFF',
      theme: {
        fill: {
          linearGradient: {
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 1
          },
          stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#5151A6' ] ]
        },
        stroke: '#C5C5E9'
      }
    }
  },
  rangeSelector: {
    buttonTheme: {
      fill: {
        linearGradient: {
          x1: 0,
          y1: 0,
          x2: 0,
          y2: 1
        },
        stops: [ [ 0.4, '#C5C5E9' ], [ 0.6, '#8D8DE4' ] ]
      },
      stroke: '#C5C5E9',
      style: {
        color: 'white',
        fontWeight: 'bold'
      },
      states: {
        hover: {
          fill: {
            linearGradient: {
              x1: 0,
              y1: 0,
              x2: 0,
              y2: 1
            },
            stops: [ [ 0.4, '#A9A9EB' ], [ 0.6, '#6363E4' ] ]
          },
          stroke: '#C5C5E9',
          style: {
            color: 'white'
          }
        },
        select: {
          fill: {
            linearGradient: {
              x1: 0,
              y1: 0,
              x2: 0,
              y2: 1
            },
            stops: [ [ 0.1, '#1D1D8C' ], [ 0.3, '#3838B9' ] ]
          },
          stroke: '#C5C5E9',
          style: {
            color: '#F2F2FA'
          }
        }
      }
    },
    inputStyle: {
      backgroundColor: '#333',
      color: '#F8F8FC'
    },
    labelStyle: {
      color: '#F8F8FC'
    }
  },

  navigator: {
    handles: {
      backgroundColor: '#58589B',
      borderColor: '#C5C5E9'
    },
    outlineColor: '#A7A7DD',
    maskFill: 'rgba(40, 40, 95, 0.5)',
    series: {
      color: '#7798BF',
      lineColor: '#A6C7ED',
      xAxis: {
        labels: {
          style: {
            color: '#F8F8FC'
          }
        }
      }
    }
  },

  scrollbar: {
    barBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#6666D1' ] ]
    },
    barBorderColor: '#C5C5E9',
    buttonArrowColor: '#C5C5E9',
    buttonBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#6666D1' ] ]
    },
    buttonBorderColor: '#C5C5E9',
    rifleColor: '#FFF',
    trackBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0, '#6666D1' ], [ 1, '#9090D3' ] ]
    },
    trackBorderColor: '#666'
  },

  // special colors for some of the
  legendBackgroundColor: 'rgba(0, 0, 0, 0.5)',
  background2: 'rgb(35, 35, 70)',
  dataLabelsColor: '#C6C6E3',
  textColor: '#C0C0C0',
  maskColor: 'rgba(255,255,255,0.3)'
};
