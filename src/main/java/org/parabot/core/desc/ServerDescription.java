package org.parabot.core.desc;

/**
 * Holds information about a server
 *
 * @author Everel
 */
public class ServerDescription implements Comparable<ServerDescription> {
    private String serverName;
    private String author;
    private double revision;

    public ServerDescription(final String serverName, final String author,
                             final double revision) {
        this.serverName = serverName;
        this.author = author;
        this.revision = revision;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getAuthor() {
        return this.author;
    }

    public double getRevision() {
        return this.revision;
    }

    @Override
    public String toString() {
        return String.format("[Server: %s, Author: %s, Revision: %.2f]",
                this.serverName, this.author, this.revision);
    }

    @Override
    public int compareTo(ServerDescription o) {
        return this.getServerName().compareTo(o.getServerName());
    }

}
