$(document).ready( function() {
  setContainerSize();
  setTimeout(updateOverlayAndFont, 500);
});

$(window).resize( function() {
  setContainerSize();
  updateOverlayAndFont();
});

var boardHeight;
function setContainerSize() {
  var boardRatio = 122/176;
  var windowRatio = window.innerWidth/window.innerHeight;
  boardHeight = (windowRatio > boardRatio)?window.innerHeight:window.innerWidth/boardRatio;
  boardHeight -= (boardHeight*0.95 > 20)?20:boardHeight*0.95;
  $("#board-container").height(boardHeight);
  $("#board-container").width(boardHeight*boardRatio);
}
function updateOverlayAndFont() {
  var overlayHeight = boardHeight*0.05;
  var smallSize = overlayHeight*.75+"px";
  $(".hexagon-overlay, .diamond-shape, .component-value").css("height", overlayHeight+"px").css("width", overlayHeight+"px");
  $(".counter-overlay, .poison-overlay").css({height: smallSize, width: smallSize, lineHeight: smallSize, fontSize: smallSize});
  $(".heroHP").css("width", "100%");
  $(".hexagon-overlay").css({lineHeight: overlayHeight+"px", fontSize: overlayHeight*0.8+"px"});
}
