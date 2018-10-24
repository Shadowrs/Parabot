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
    public int uuid;
    private String updated;
    private boolean active;

    public ServerDescription(final String serverName, final String author,
                             final double revision) {
        this.serverName = serverName;
        this.author = author;
        this.revision = revision;
        this.updated = "Unknown";
    }

    public ServerDescription(final String serverName, final String author,
                             final double revision, String updated, boolean active) {
        this.serverName = serverName;
        this.author = author;
        this.revision = revision;
        this.updated = updated == null ? "Unknown" : updated;
        this.active = active;
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

    public boolean getActive() {
        return this.active;
    }

    public String getUpdated() {
        return this.updated;
    }

    @Override
    public String toString() {
        return String.format("[Server: %s, Author: %s, Revision: %.2f]",
                this.serverName, this.author, this.revision);
    }

    @Override
    public int compareTo(ServerDescription o) {
        if (this.getServerName().equalsIgnoreCase(o.getServerName())) {
            if (getAuthor().equals(o.getAuthor())) {
                return Double.compare(o.getRevision(), getRevision());
            }
            return getAuthor().compareTo(o.getAuthor());
        }
        return this.getServerName().compareTo(o.getServerName());
    }
}
