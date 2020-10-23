
// Modify GameViewModel's click binding to register double clicks only
_gameViewModel.usingDoubleClick = true;

_gameViewModel.clicked = function(object, x, y) {
  var self = _gameViewModel;
  var clicked = !!self.clickedObj;
  // detect invalid action
  if (!isValidAction(x(),y())) {
    self.undoAction();
    return;
  }
  if (clicked) {
    // process double click
    if (self.clickedObj === object) {
      var action = getAction(x(), y());
      // if action: send ajax and wait for response
      if (action) {
        self.updateBoard(action);
        return;
      }
      //...else continue below
    }
    // show previously hidden image
    self.undoAction();
  }
  // always hide newly clicked image
  object.realName = object.name;
  object.name = "";
  self.clickedObj = object;
  self.refresh();
}

// Adding other support functions required for double click
function isValidAction(x, y, size) {
  return (getAction(x, y, size)!=null);
}

// Translate position of double click into user action
function getAction(x, y, size) {
  var size = (size)?size:5;
  var center = Math.floor(size/2);
  var xOffset = x - center;
  var yOffset = y - center;
  if (Math.abs(xOffset) === Math.abs(yOffset)) {
    return null;
  }
  else if (Math.abs(xOffset) > Math.abs(yOffset)) {
    if (xOffset > 0) return "Right";
    return "Left";
  }
  else {
    if (yOffset > 0) return "Down";
    return "Up";
  }
}

// Undo action, update display
_gameViewModel.undoAction = function() {
  var self = _gameViewModel;
  if (!!self.clickedObj) {
    self.clickedObj.name = self.clickedObj.realName;
    self.clickedObj = undefined;
    self.refresh();
  }
}
// Update display
_gameViewModel.refresh = function() {
  var self = _gameViewModel;
  var a=self.board();self.board([]);self.board(a);
}
