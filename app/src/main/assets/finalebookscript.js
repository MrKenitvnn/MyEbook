
Monocle.Events.listen(
window,
'load',
function () {

  var readerOptions = {};
  /* PLACE SAVER */
  var bkTitle = bookData.getMetaData('title');
  var placeSaver = new Monocle.Controls.PlaceSaver(bkTitle);
  readerOptions.place = placeSaver.savedPlace();
//  readerOptions.panels = Monocle.Panels.Magic;
  readerOptions.panels = Monocle.Panels.IMode;
  readerOptions.stylesheet = "body { " +
	"color: #210;" +
	"font-family: Palatino, Georgia, serif;" +
  "}";

  /* Initialize the reader */
  window.reader = Monocle.Reader(
	'reader',
	bookData,
	readerOptions,
	function(reader) {
