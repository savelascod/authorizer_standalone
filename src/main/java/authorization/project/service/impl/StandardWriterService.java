package authorization.project.service.impl;

import authorization.project.service.contract.IWriterService;

/**
 * Service implementation for {@link IWriterService}
 *
 * @version 1.0
 * @since 1.0
 */
public class StandardWriterService implements IWriterService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeWelcomeMessage() {
        System.out.println("################################# WELCOME :) ################################");
        System.out.println("POSSIBLE INPUTS:");
        System.out.println("1 -> ARRAY OF JSON OBJECTS: [{account-creation},{transaction},{transaction}]");
        System.out.println("2 -> JSON OBJECT: {account-creation}");
        System.out.println("3 -> ANYTHING ELSE TO EXIT");
        System.out.println("############################################################################");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeResponse(String response) {
        System.out.println(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExitMessage() {
        System.out.println("################## PROCESS TERMINATED, HASTA LA VISTA BABY #################");
    }

}
