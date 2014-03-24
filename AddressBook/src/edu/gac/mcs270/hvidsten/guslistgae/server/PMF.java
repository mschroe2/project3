/*
 * Class to instantiate a single PersistenceManager
 */

package edu.gac.mcs270.hvidsten.guslistgae.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
 private static final PersistenceManagerFactory pmfInstance =
     JDOHelper.getPersistenceManagerFactory("transactions-optional");

 private PMF() {}

 public static PersistenceManagerFactory get() {
   return pmfInstance;
 }
}