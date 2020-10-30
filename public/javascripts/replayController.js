function gameViewModel() {
  var self = this;
  // init
  self.playerName;
  self.gameId;
  // getMoveCount
  self.lastIndex;
  // updateGameState
  self.board = ko.observableArray();
  self.currentIndex;
  // stats
  self.isFogged = ko.observable();
  self.bossSpawnCounter;
  self.stepsWalked;
  self.bossesKilled;
  self.monstersKilled;

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
      self.updateStats();
    });
  }

  self.updateBoard = function(direction) {
    switch (direction) {
      case "up":
        self.showStats();
        break;
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

  self.updateStats = function() {
    var hero = self.findHero();
    self.isFogged(hero.fogCounter > 0);
    self.bossSpawnCounter = 10-hero.timeSinceBossKill;
    self.stepsWalked = hero.stepsWalked;
    self.bossesKilled = hero.bossesKilled;
    self.monstersKilled = hero.monstersKilled;
  }

  self.clicked = function(objectClicked, x, y) {
    if (objectClicked.type === "HERO") {
      self.updateBoard("up");
    }
    else if (x() < 2) {
      self.updateBoard("left");
    }
    else if (x() == 2) {
      self.updateBoard("down");
    }
    else {
      self.updateBoard("right");
    }
  }

  self.showStats = function() {
    var stats = "";
    if (self.bossSpawnCounter > 0) {
      stats += "Boss will spawn in: " + self.bossSpawnCounter + " turn";
      stats += (self.bossSpawnCounter == 1)?"":"s";
      stats += "\n\n";
    }
    stats += "Steps Taken: " + self.stepsWalked;
    stats += "\nMonsters Killed: " + self.monstersKilled;
    stats += "\nBosses Killed: " + self.bossesKilled;
    alert(stats);
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
      if (self.isFogged() && object.type !== "HERO") {
        return "/assets/images/empty1.png";
      }
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

  self.isHero = function(object) {
    return object.type === "HERO";
  }

  self.showValue = function(object) {
    if (self.isFogged()) return false;
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

  self.showCounter = function(object) {
    if (self.isFogged() && !self.isHero(object)) {
      return false;
    }
    if (object.counter) {
      return true;
    }
    return false;
  }

  self.showArmour = function(object) {
    if (self.isFogged() && !self.isHero(object)) {
      return false;
    }
    if (object.armour) {
      return true;
    }
    return false;
  }

  self.findHero = function() {
    var board = self.board();
    for (var y = 0; y < board.length; y++) {
      for (var x = 0; x < board[0].length; x++) {
        if (board[y][x].maxHealth)
          return board[y][x];
      }
    }
  }

  self.init();
}

var _gameViewModel = new gameViewModel();
ko.applyBindings(_gameViewModel);
