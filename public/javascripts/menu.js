var playerName;

$("#playerForm > input").keyup(function(e) {
  playerName = $(this).serializeArray()[0].value.trim();
  $(this).siblings("button[type='submit']")[0].disabled = (playerName == "");
});

$("#playerForm").submit(function(e) {
  $(this).hide();
  $("#gameOptions > #name").text(playerName);
  $("#gameOptions").show();
  e.preventDefault();
});


