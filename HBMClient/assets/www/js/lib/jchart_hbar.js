var jchart_hbar = function(){

  var width = window.innerWidth; 

  var padding = {left:10,right:20,top:20,bottom:20};
    
  var ydataset = [],xdataset = [],_IDp;
  
  var anchor=["用户数(单位:户)"];
    
  var rectPadding = 4;
    
function randomData() {
    return Math.random() * -1000000 | 0 ;  //取整
}

function _update() {

    xdataset.length = 0;
    for (var j = 0; j < 17; ++j)
        xdataset.push(randomData());    

    $( '#' + _IDp ).empty();
    $( '#' + _IDp ).append('<p><a></a><span></span></p>');

    if( (d3.max(xdataset) == 0 &&  d3.min(xdataset) ==0)|| xdataset.length == 0 ){
        console.log( '数据为空' );
    }else{
        if(d3.max(xdataset) < 0 ){
            _renderminus(400);    
        }else if( d3.min(xdataset) > 0 ){
             _renderplus(400);
        }else{
            _renderall( 400 );
        }
    }
}

function _init(IDp,tpara,xdata,ydata){
    
    xdataset = xdata;
    ydataset = ydata;
    _IDp = IDp;
    
    anchor.length = 0;
    anchor.push( tpara );
    
    $( '#' + _IDp ).empty();
    $( '#' + _IDp ).append('<p><a></a><span></span></p>');
    
    if( (d3.max(xdataset) == 0 &&  d3.min(xdataset) ==0)|| xdataset.length ==0 ){
        console.log( '数据为空' );
    }else{
        if(d3.max( xdataset ) <= 0 ){
            _renderminus( 400 );    
        }else if( d3.min( xdataset ) >= 0 ){
             _renderplus( 400 );
        }else{
            _renderall( 400 );
        }
    }
}

function _renderminus( _height ){
     padding.left = 10;
    
    var vmax;
    if( d3.min(xdataset) != d3.max(xdataset) ){
        
        if( (d3.max(xdataset) - d3.min(xdataset))/2 < 0-d3.max(xdataset)*2/5){
            vmax = d3.max(xdataset) + (d3.max(xdataset) - d3.min(xdataset))*2/5
        }else{
            vmax = d3.max(xdataset)*3/5;
        }
        
    }else{
        vmax = 0;
    }
    
	var svg = d3.select('#' + _IDp)
		.append("svg")
		.attr("width", width)
		.attr("height", _height );
    
	var xScale = d3.scale.linear()
		.domain([0,d3.min(xdataset)]) 
		.range([width - padding.left - padding.right,0]);
    
    var yScale = d3.scale.ordinal()
            .domain(d3.range(xdataset.length))
            .rangeRoundBands([0,_height - padding.top - padding.bottom]);

	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom")
        .ticks(5)
		
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("left")
        .ticks(5)
        .tickFormat(function(v,i){
            return ydataset[i];
        });
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(xdataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
            var min = xScale.domain()[0];
            return xScale(min);
		} )
		.attr("width", function(d){
			 return 0;
		})
		.attr("y",function(d,i){
            return yScale(i) + rectPadding/2;
		})
		.attr("height",function(d){
             return yScale.rangeBand() - rectPadding;   
        })
        .on("tap",function(d,i){
            
            var td,tdv;
            if( d < -100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if(  d < -10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d ;
                td = td.toFixed(2);
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:green;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:#2573fe;font-size:15px");
            
                p.style("color","green");
                d3.select(this)
				    .attr("fill","green");
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("x",function(d){
                return xScale(d);
		})
		.attr("width", function(d){
                return (width - padding.left - padding.right) - xScale(d);

		})
        .attr("fill","#ff7f50");
    
    svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x", padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});   
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + (_height - padding.bottom) + ")")
		.call(xAxis); 
		
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + (width-padding.right) + "," + padding.top + ")")
		.call(yAxis);
}    
function _renderplus( _height ){
     padding.left = 5;
    
    var vmin;
    if( d3.min(xdataset) != d3.max(xdataset) ){
        
        if( (d3.max(xdataset) - d3.min(xdataset))/2 < d3.min(xdataset)*2/5){
            vmin = d3.min(xdataset) - (d3.max(xdataset) - d3.min(xdataset))*2/5
        }else{
            vmin = d3.min(xdataset)*3/5;
        }
        
    }else{
        vmin = 0;
    }
    
	var svg = d3.select('#' + _IDp)
		.append("svg")
		.attr("width", width)
		.attr("height", _height );

	var xScale = d3.scale.linear()
		.domain([0,d3.max(xdataset)]) 
		.range([0,width - padding.left - padding.right]);

    var yScale = d3.scale.ordinal()
            .domain(d3.range(xdataset.length))
            .rangeRoundBands([0,_height - padding.top - padding.bottom]);
    
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom")
        .ticks(5)
		
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("right")
        .ticks(5)
        .tickFormat(function(v,i){
            return ydataset[i];
        });
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(xdataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
            return 0;
		} )
		.attr("width", function(d){
			 return 0;
		})
		.attr("y",function(d,i){
            return yScale(i) + rectPadding/2;
		})
		.attr("height",function(d){
             return yScale.rangeBand() - rectPadding;   
        })
        .on("tap",function(d,i){
            
            var td,tdv;
            if( d > 100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if( d > 10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d ;
                td = td.toFixed(2);
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:red;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:blue;font-size:15px");

                p.style("color","red");
                d3.select(this)
				    .attr("fill","red");
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("x",function(d){
            return 0;
		})
		.attr("width", function(d){
                return  xScale(d);
		})
        .attr("fill","#ff7f50");
    
    svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x", padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + (_height - padding.bottom) + ")")
		.call(xAxis); 
		
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(yAxis);
}     
function _renderall( _height ){
     padding.left = 5;
	var svg = d3.select('#' + _IDp)
		.append("svg")
		.attr("width", width)
		.attr("height", _height );

	var xScale = d3.scale.linear()
		.domain([0,d3.max(xdataset)]) 
		.range([0, width/2 -padding.right]);

    var xScale1 = d3.scale.linear()
        .domain([d3.min(xdataset),0]) 
        .range([0, width/2 -padding.left ]);
    
    var yScale = d3.scale.ordinal()
            .domain(d3.range(xdataset.length))
            .rangeRoundBands([0,_height - padding.top - padding.bottom]);
    
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom")
        .ticks(3)

	var xAxis1 = d3.svg.axis()
		.scale(xScale1)
		.orient("bottom")
        .ticks(3)
    
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("right")
        .ticks(5)
        .tickFormat(function(v,i){
            return ydataset[i];
        });
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(xdataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + width/2 + "," + padding.top + ")")
		.attr("x", function(d,i){
            if( d >=0 ){
                return 0;
            }else{
                return 0;
            }
		} )
		.attr("width", function(d){
			 return 0;
		})
		.attr("y",function(d,i){
            return yScale(i) + rectPadding/2;
		})
		.attr("height",function(d){
             return yScale.rangeBand() - rectPadding;   
        })
        .on("tap",function(d,i){

            var td,tdv;

            if( d < -100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if(  d < -10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else if( d < 10000 ){
                td = d ;
                td = td.toFixed(2);
            }else if( d < 100000000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:red;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:blue;font-size:15px");
            
            if( d < 0 ){
                a.style("color","green");
                d3.select(this)
				    .attr("fill","green");
            }else{
                d3.select(this)
				    .attr("fill","red");
            }
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("x",function(d){
            if( d >=0 ){
                return 0;
            }else{
                return (-width/2 +padding.left +xScale1(d));
            }
		})
		.attr("width", function(d){
            if( d >= 0 ){
                return  xScale(d);
            }else{
                return  width/2 -padding.left - xScale1(d);
            }
		})
        .attr("fill","#ff7f50");
    
    svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x", padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" +  width/2 + "," + (_height - padding.bottom) + ")")
		.call(xAxis); 
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" +  padding.left + "," + (_height - padding.bottom) + ")")
		.call(xAxis1); 
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + width/2 + "," + padding.top + ")")
		.call(yAxis);
}  
    
    return {
        init: _init,
        update: _update
    }
    
}();