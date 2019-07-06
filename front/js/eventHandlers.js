$(document).ready(function() {
	$(".btn").click(function() {
        var data = {};
        data = { "text" : $("textarea").val() };
		$("textarea").toggleClass("loading");
		$("textarea").prop("disabled", true);
		$(".btn").toggleClass("disabled");
        $.ajax({
            type: "POST",
            data: data,
			cache: false,
            url: "Corrector",
            success: function(serverData) {
				var htmlCode = "";
				var j;
				var i;
				for (i = 0; i < serverData.length; ++i) {
					htmlCode += "<span id='" + serverData[i].id + "'";
					if (serverData[i].incorrect) {
						htmlCode += " class='incorrect'><span class='error'>" + serverData[i].word + "</span><div class='dropdown'><div id='" + serverData[i].id + "c" + "' class='dropdown-content'>";
						for (j = 0; j < serverData[i].corrections.length; j++)
							htmlCode += "<a class='correction' id='" + serverData[i].id + "c" + j + "'>" + serverData[i].corrections[j] + "</a>";
						htmlCode += "</div></div></span>";
					}
					else {
						htmlCode += ">";
						for (j = 0; j < serverData[i].word.length; ++j)
							switch (serverData[i].word[j]) {
								case "\n":
									htmlCode += "<br>";
									break;
								case " ":
									htmlCode += "&nbsp";
									break;
								case "\t":
									htmlCode += "&nbsp&nbsp&nbsp&nbsp";
									break;
								default:
									htmlCode += serverData[i].word[j];
							}
						htmlCode += "</span>";
					}
				}
				$(".correction-container").html(htmlCode);
				$(".correction-container").attr("style", null);
				$("html, body").animate({scrollTop: $(".correction-container").offset().top - 30 + 'px'});
				$("textarea").toggleClass("loading");
				$(".btn").toggleClass("disabled");	
				$("textarea").prop("disabled", false);				
            },
            error: function(e) {
				$("textarea").toggleClass("loading");
				$(".btn").toggleClass("disabled");
				$("textarea").prop("disabled", false);	
                alert("Ошибка");
            }
        });
    });
	
	$(document).on("click", ".incorrect", function() {
		$("#" + $(this).attr("id") + "c").toggleClass("show");
	});
	
	$(document).on("click", ".correction", function() {
		var wordId = $(this).attr("id").split("c")[0];
		if ($(this).text()[$(this).text().length - 1] === "." && $("#" + (parseInt(wordId) + 1)).text()[0] === ".") {
			$("#" + wordId).text($(this).text().substring(0, $(this).text().length - 1));
			$("#" + wordId).removeClass("incorrect");
			$("#" + wordId + "c").toggleClass("show");
		}
		else {
			$("#" + wordId).text($(this).text());
			$("#" + wordId).removeClass("incorrect");
			$("#" + wordId + "c").toggleClass("show");
		}
	});
});