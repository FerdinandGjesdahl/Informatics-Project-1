import Cookies from "universal-cookie";

import networkPackets from '../protos/network_packets_pb';

/**
 * Helper functions for session management
 */
class SessionManager {

    /**
     * Sets the session cookies for the user
     * @param {UserSession} userSession Session to set
     * @returns {void} Nothing
     */
    static setSessionCookies(userSession) {
        const cookies = new Cookies();
        cookies.set("username", userSession.getUsername())
        cookies.set("secret", userSession.getSecret())
    }


    /**
     * Gets the session cookies for the user
     * @returns {UserSession} UserSession object of the session. null if no cookie set.
     */
    static getSessionFromCookies() {
        const cookies = new Cookies();
        const username = cookies.get("username")
        const secret = cookies.get("secret")

        if (username === undefined || secret === undefined) {
            return null
        }

        const userSession = new networkPackets.UserSession()
        userSession.setUsername(username)
        userSession.setSecret(secret)

        return userSession
    }

    /**
     * Clears the session cookies for the user
     * @returns {void}
     */
    static clearSessionCookies() {
        const cookies = new Cookies();
        cookies.remove("username")
        cookies.remove("secret")
    }

    /**
     * Validates the session cookies for the user
     * @param {UserSession} userSession Session to validate
     * @returns {Promise<boolean>} True if the session is valid, false otherwise
     */
    static validateSession = async (userSession) => {
        if (userSession === null) {
            return false
        }

        try {
            await fetch("http://localhost:8080/userapi/sessionValid", {
                method: "POST",
                headers: {
                    "Accept": "application/x-protobuf",
                    "Content-Type": "application/x-protobuf"
                },
                body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok) {
                    if (response.status === 403){
                        console.log("Session was not valid.")
                        return false
                    }

                    console.log("Response was not ok.")
                    console.log("HTTP error: " + response.status)
                    return false
                }
                return response.arrayBuffer()
            }).then((buffer) => {
                if (buffer === false){
                    return false
                }
                const session = networkPackets.UserSession.deserializeBinary(buffer)
                if (session.getUsername() === userSession.getUsername() &&
                    session.getSecret() === userSession.getSecret()) {
                    console.log("Session is valid")
                    return true;
                } else {
                    console.log("Validated sessions were not equal")
                    return false;
                }
            })


        } catch (error) {
            console.log("Failed to validate session")
            console.log(error)
            return false
        }
    }
}

export default SessionManager;
