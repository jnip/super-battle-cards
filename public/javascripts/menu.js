var playerName;

$(document).ready(function() {
  var $input = $("#playerForm > input");
  $input.focus(); 
  playerName = $input.serializeArray()[0].value.trim();
  $("button[type='submit']")[0].disabled = (playerName == "");
});

$("#playerForm > input").keyup(function(e) {
  playerName = $(this).serializeArray()[0].value.trim();
  $(this).siblings("button[type='submit']")[0].disabled = (playerName == "");
});

$("#playerForm").submit(function(e) {
  $("input").blur(); // hide keyboard
  $(this).hide();
  $("#gameOptions > #name").text(playerName);
  $("#gameOptions").show();
  e.preventDefault();
});

$("#gameOptions").submit(function(e){ e.preventDefault(); });
$("#continue").click(function(){ window.location = "/continue/" + playerName; });
$("#new").click(function(){ window.location = "/new/" + playerName; });

$("#replay").click(function() {
  $.getJSON("/replayData/"+self.playerName, function(gameCount) {
      if (gameCount < 1) {
        alert("Sorry, no games found.");
        return;
      }
      else {
        var gameId = -1;
        if (gameCount > 1) {
          var choosenId;
          do {
            var input = prompt("Which game would you like to replay?\n"+
              "Pick a number between 0-"+(gameCount-1));
            if (input == null) {
              return;
            }
            choosenId = parseInt(input);
          }
          while (isNaN(choosenId) || choosenId < 0 || choosenId > gameCount-1);
          gameId = choosenId;
        }
        window.location = "/replay/" + self.playerName + "/" + gameId;
      }
    });
});
