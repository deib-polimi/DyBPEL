// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/xpath/ast/AeXPathPathExprNode.java,v 1.1 2009/09/23 13:09:42 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.xpath.ast;

/**
 * An XPath node for a path expression.
 */
public class AeXPathPathExprNode extends AeAbstractXPathNode
{
   /**
    * Default c'tor.
    */
   public AeXPathPathExprNode()
   {
      super(AeAbstractXPathNode.NODE_TYPE_PATH_EXPR);
   }
   
   /**
    * @see org.activebpel.rt.bpel.xpath.ast.AeAbstractXPathNode#normalize()
    */
   public AeAbstractXPathNode normalize()
   {
      return normalizeOmitSelf();
   }
   
   /**
    * @see org.activebpel.rt.bpel.xpath.ast.AeAbstractXPathNode#accept(org.activebpel.rt.bpel.xpath.ast.IAeXPathNodeVisitor)
    */
   public void accept(IAeXPathNodeVisitor aVisitor)
   {
      aVisitor.visit(this);
   }
}
