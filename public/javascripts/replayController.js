function gameViewModel() {
  var self = this;
  self.board = ko.observableArray();
  self.playerName;
  self.gameId;
  self.lastIndex;
  self.currentIndex;

  self.init = function() {
    var pathValues = window.location.pathname.split("/");
    self.playerName = pathValues[2];
    self.gameId = pathValues[3];
    self.getMoveCount();
  }

  self.getMoveCount = function() {
    var ajaxPath = "/replayData/"+self.playerName+"/"+self.gameId;
    $.getJSON(ajaxPath, function(moveIndex) {
      self.lastIndex = parseInt(moveIndex);
      if (self.board().length == 0) {
        self.updateGameState(self.lastIndex);
      }
    });
  }

  self.updateGameState = function(moveIndex) {
    var ajaxPath = "/replayData/"+self.playerName+"/"+self.gameId + "/"+ moveIndex;
    $.getJSON(ajaxPath, function(data) {
      self.board(data);
      self.currentIndex = moveIndex;
    });
  }

  self.updateBoard = function(direction) {
    switch (direction) {
      case "up":
      case "down":
        self.showMenu();
        break;
      case "left":
        if (self.currentIndex == 0) {
          alert("Beginning of the game.\nCannot view previous.");
        }
        else {
          self.updateGameState(self.currentIndex-1);
        }
        break;
      case "right":
        if (self.currentIndex == self.lastIndex) {
          alert("End of the game.\nCannot view next.");
        }
        else {
          self.updateGameState(self.currentIndex+1);
        }
        break;
      default:
    }
  }

  self.clicked = function(objectClicked, x, y) {
    if (x() < 2) {
      self.updateBoard("left");
    }
    else if (x() == 2) {
      self.updateBoard("up");
    }
    else {
      self.updateBoard("right");
    }
  }

  self.showMenu = function() {
    var choosenIndex;
    do {
      var input = prompt("Viewing Move #" + self.currentIndex +
      "\nEnter a number from 0-" + self.lastIndex);
      if (input == null) {
        return;
      }
      choosenIndex = parseInt(input);
    }
    while (isNaN(choosenIndex) || choosenIndex < 0 || choosenIndex > self.lastIndex);
    self.updateGameState(choosenIndex);
  }

  self.getImageLink = function(object) {
    if (object && object.name) {
      var name;
      if (object.type == "ITEM" || object.type == "SKILL") {
        name=(object.state==1)?"barrel":object.name;
      }
      else {
        name=object.name+((isNaN(object.state))?"":object.state);
      }
      return "/assets/images/SBC_"+name+".png";
    }
    return "/assets/images/empty.png";
  }

  self.showImageValue = function(object) {
    if (object.health) return true;
    if (object.value && object.value != 0) {
      if (object.name == "Spike") return !!object.state;
      if (object.type=="ITEM"||object.type=="SKILL") return !object.state;
      return true;
    }
    else {
      return false;
    }
  }

  self.init();
}

var _gameViewModel = new gameViewModel();
ko.applyBindings(_gameViewModel);
