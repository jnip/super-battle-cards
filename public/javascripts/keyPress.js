
// Add keyboard press detection to GameViewModel
_gameViewModel.onKeyPress = function(event) {
  var self = _gameViewModel;
  // Arrow Down
  if (event.which == 40) {
    self.updateBoard("down");
  }
  // Arrow Right
  if (event.which == 39) {
    self.updateBoard("right");
  }
  // Arrow Up
  if (event.which == 38) {
    self.updateBoard("up");
  }
  // Arrow Left
  if (event.which == 37) {
    self.updateBoard("left");
  }
}
$(document).keydown(_gameViewModel.onKeyPress);
