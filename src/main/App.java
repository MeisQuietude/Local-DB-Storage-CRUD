package main;

/**
 * The App program implements as CRUD-application that simply do 4 storage operation:
 * Create, Read, Update, Delete.
 *
 * @author Stepan Savelyev
 * @version 0.9
 * @since 2019-03-04
 */

class App {

    private App() {
        /* Prepare */
            if (!prepareStart()) {
            System.out.println("There is a problem... Break.");
            System.exit(1);
        }
        System.out.println("Hello, neighbour.. It is a time to looking your storage ;)");

        /* Action */
        User.UserInterface user = new User.UserInterface();
        user.selectMainAction();

        /* End */
        System.exit(0);
    }

    public static void main(String[] args) {
        new App();
    }

    private boolean prepareStart() {

        if (!FileOperator.Additional.validateMetaFile()) {
            Data.Processing.createDB();
        }

        return true;
    }


}