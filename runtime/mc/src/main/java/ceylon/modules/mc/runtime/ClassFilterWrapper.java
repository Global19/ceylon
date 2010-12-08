/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package ceylon.modules.mc.runtime;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.filter.ClassFilter;

import ceylon.lang.modules.PathFilter;

/**
 * ClassFilter wrapper based on PathFilter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class ClassFilterWrapper implements ClassFilter
{
   private PathFilter filter;

   ClassFilterWrapper(PathFilter filter)
   {
      this.filter = filter;
   }

   public boolean matchesClassName(String className)
   {
      return matchesResourcePath(ClassLoaderUtils.classNameToPath(className));
   }

   public boolean matchesResourcePath(String resourcePath)
   {
      return filter.accept(resourcePath);
   }

   public boolean matchesPackageName(String packageName)
   {
      return matchesResourcePath(ClassLoaderUtils.packageToPath(packageName));
   }

   public String toString()
   {
      return filter.toString();
   }
}