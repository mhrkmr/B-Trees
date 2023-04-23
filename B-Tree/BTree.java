package col106.a3;

import java.util.List;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> 
{
	int m;
	Node<Key,Value> root;
    public BTree(int b) throws bNotEvenException 
    {   /* Initializes an empty b-tree. Assume b is even. */
        //throw new RuntimeException("Not Implemented");
    	if (b % 2==1)
    	{
    		throw new bNotEvenException();
    	}
    	m=b;
    	root=new Node<Key,Value>();    	
    }

    @Override
    public boolean isEmpty() 
    {
    	if(root.nextfirst==null && root.ar.size()==0)
    	{
    		return true;
    	}
    	return false;
        //throw new RuntimeException("Not Implemented");
    }

    @Override
    public int size() 
    {    
    	if(isEmpty())
    	{
    		return -1;
    	}
    	return root.recSize();
        //throw new RuntimeException("Not Implemented");
    }

    @Override
    public int height() 
    {
    	int i=0;
    	Node<Key,Value> temp=root;
    	while(temp.nextfirst!=null)
    	{
    		temp=temp.nextfirst;
    		i++;
    	}
    	return i;
        //throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException 
    {
    	return root.recSearch(key);
    	//throw new RuntimeException("Not Implemented");
    }

    @Override
    public void insert(Key key, Value val) 
    {
    	root.recIn(key,val,m);
        //throw new RuntimeException("Not Implemented");
    }
    
    @Override
    public void delete(Key key) throws IllegalKeyException 
    {
    	if(root.recSearch(key).size()==0)
    	{
    		return; 
    	}
    	int n=root.recSearch(key).size();
		for(int i=0;i<n;i++)
		{
			root.recDelete(key,m);
		}
    	//throw new RuntimeException("Not Implemented");
    }
    
    
    @Override
    public String toString()
    {
    	return root.recString();
    }
}
