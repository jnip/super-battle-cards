// 5 x 5 board
function gameViewModel() {
  var self = this;
  // updateBoard
  self.board = ko.observableArray();
  // init
  self.playerName;
  self.gameId;
  // stats
  self.isFogged = ko.observable();
  self.bossSpawnCounter;
  self.stepsWalked;
  self.bossesKilled;
  self.monstersKilled;
  self.money;

  self.init = function() {
    // Get playerName nad gameId from the url
    var pathValues = window.location.pathname.split("/");
    self.playerName = pathValues[1];
    self.gameId = pathValues[2];
    self.updateBoard();
  }

  // ajax to server
  self.updateBoard = function(action, objectClicked) {
    if (self.isLoadingAction) { return; }
    // ensure we are not making a move when game is already over
    if (action) {
      var hero = self.findHero();
      if (hero == null) { action = undefined; }
      else if (hero.health <= 0) { self.gameOver(); return; } 
    }
    var ajaxPath = "/play/"+self.playerName+"/"+self.gameId+((action)?"?move="+action:"");
    $.getJSON(ajaxPath, function(data) {
      self.clickedObj = undefined;
      self.board(data);
      self.updateStats();
      self.isLoadingAction = false;
    })
    .fail(function() {
      alert("Failed to load data.\nRedirecting to menu...");
      window.location = "/";
    });
    
    if (!self.usingDoubleClick && action && objectClicked) {
      // UI Replace: clicked tile with empty white tile
      objectClicked.name = "";
      var temp=self.board();self.board([]);self.board(temp);

      // don't register actions when waiting for reply from server
      self.isLoadingAction = true;
    }
  }

  self.updateStats = function() {
    var hero = self.findHero();
    self.isFogged(hero.fogCounter > 0);
    self.bossSpawnCounter = 10-hero.timeSinceBossKill;
    self.stepsWalked = hero.stepsWalked;
    self.bossesKilled = hero.bossesKilled;
    self.monstersKilled = hero.monstersKilled;
    self.money = hero.money;
  }

  self.showStats = function() {
    var stats = "";
    if (self.bossSpawnCounter > 0) {
      stats += "Boss will spawn in: " + self.bossSpawnCounter + " turn";
      stats += (self.bossSpawnCounter == 1)?"":"s";
      stats += "\n\n";
    }
    stats += "Steps Taken: " + self.stepsWalked;
    stats += "\nGold Collected: " + self.money;
    stats += "\nMonsters Killed: " + self.monstersKilled;
    stats += "\nBosses Killed: " + self.bossesKilled;
    alert(stats);
  }

  // Map click to player move action
  self.clicked = function(objectClicked, x, y) {
    if (objectClicked.type === "HERO") {
      self.showStats();
      return;
    }
    var action = self.isHeroNearby(x(), y());
    if (action) {
      self.updateBoard(action, objectClicked);
    }
    else {
      // Ignore clicks that are not next to hero
    }
  }

  self.isHeroNearby = function(x, y) {
    var action;
    
    if (isHeroAtPosition(x-1, y)) {
      // Hero on the left of clicked position
      action = "right"; // So we are moving right
    }
    if (isHeroAtPosition(x+1, y)) { action = "left"; }
    if (isHeroAtPosition(x, y-1)) {
      // Hero above clicked position
      action = "down"; // So Hero is moving down to clicked position
    }
    if (isHeroAtPosition(x, y+1)) { action = "up"; }

    return action;

    function isHeroAtPosition(x, y) {
      if (x < 0 || y < 0) { return false; }
      if (x >= self.board()[0].length || y >= self.board().length) { return false; }
      return self.board()[y][x].maxHealth != undefined;
    }
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

  self.gameOver = function() {
    var newGame = confirm("Game Over!\nStart a New Game?");
    if (newGame) {
      window.location = "/new/" + self.playerName;
    }
  }

  self.init();
}

var _gameViewModel = new gameViewModel();
ko.applyBindings(_gameViewModel);
