
$(window).resize(function() {
  var boardRatio = 122/176;
  var windowRatio = $(window).width()/$(window).height();
  var boardHeight = (windowRatio > boardRatio)?"100%":$(window).width()/boardRatio+"px";
  $("#board-container").height(boardHeight);
});
