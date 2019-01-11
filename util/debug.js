
module.exports = (type, message) => {
  //* Return and don't log if not NODE_ENV != dev
  if(process.env.NODE_ENV != "dev") return

  //* Require chalk for fancier debug
  var chalk = require('chalk')

  //* Switch through types
  switch(type) {
    case "info":
      console.log(chalk.hex("#5050ff")(message))
      break;
    case "success":
      console.log(chalk.hex("#50ff50")(message))
      break;
    case "error":
      console.log(chalk.hex("#ff5050")(message))
      break;
  }
}