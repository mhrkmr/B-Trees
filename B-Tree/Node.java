package col106.a3;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Node<Key extends Comparable<Key>, Value>
{
	ArrayList<Key> ar=new ArrayList<Key>();
	ArrayList<Value> arv=new ArrayList<Value>();
	ArrayList<Node<Key,Value>> next=new ArrayList<Node<Key,Value>>();
	Node<Key,Value> nextfirst=null;
	Node<Key,Value> prev=null;
	
	public void Insertsorted(Key key,Value value,Node<Key,Value> node) 
	{
		if(node!=null)
		{
			node.prev=this;
		}
		for(int i=0;i<ar.size();i++)
		{
			if(key.compareTo(ar.get(i))<=0)
			{
				ar.add(i, key);
				arv.add(i, value);
				next.add(i,node);
				return;
			}
		}
		ar.add(key);
		arv.add(value);
		next.add(node);
	}
	
	public int recSize()
	{
		int s=0;
		if(nextfirst==null)
		{
			return ar.size();
		}
		s=s+ar.size()+nextfirst.recSize();
		
		for(int i=0;i<ar.size();i++)
		{
			s=s+next.get(i).recSize();
		}
		
		return s;
	}
	
	public String recString()
	{
		String s="";
		if(nextfirst==null)
		{
			if(ar.size()==0)
			{
				return "[]";
			}
			s=s+"[";
			for(int i=0;i<ar.size()-1;i++)
			{
				s=s+ar.get(i).toString()+"="+arv.get(i).toString()+", ";
			}
			s=s+ar.get(ar.size()-1).toString()+"="+arv.get(ar.size()-1).toString()+"]";
			return s;
		}
		s=s+nextfirst.recString();
		for(int i=0;i<ar.size();i++)
		{
			s=s+", "+ar.get(i).toString()+"="+arv.get(i).toString()+", "+next.get(i).recString();
		}
		s= "["+s+"]";
		return s;
	}
	
	public List<Value> recSearch(Key key)
	{
		List<Value> ret=new Vector<Value>();
		if(nextfirst==null)
		{
			if(ar.contains(key))
			{
				for(int i=ar.indexOf(key);i<=ar.lastIndexOf(key);i++)
				{
					ret.add(arv.get(i));
				}
			}
			return ret;
		}
		if(ar.contains(key))
		{
			if(ar.indexOf(key)==0)
			{
				ret.addAll(nextfirst.recSearch(key));
			}
			else
			{
				ret.addAll(next.get(ar.indexOf(key)-1).recSearch(key));
			}
			for(int i=ar.indexOf(key);i<=ar.lastIndexOf(key);i++)
			{
				ret.add(arv.get(i));
				ret.addAll(next.get(i).recSearch(key));
			}
		}
		else
		{
			if(key.compareTo(ar.get(ar.size()-1))>0)
        	{
				ret.addAll(next.get(ar.size()-1).recSearch(key));
        		return ret;
        	}
        	for(int i=0;i<ar.size();i++)
    		{
    			if(key.compareTo(ar.get(i))<0)
    			{
    				if(i==0)
    				{
    					ret.addAll(nextfirst.recSearch(key));
    					return ret;
    				}
    				ret.addAll(next.get(i-1).recSearch(key));
    				return ret;
    			}
    		}
		}
		return ret;
	}
	
	public void recIn(Key key, Value value,int s)
    {
    	if(nextfirst==null)
    	{
    		Insertsorted(key,value,null);
    		if(ar.size()==s)
    		{
    			overflow(s);
    		}
    		return;
    	}
    	if(key.compareTo(ar.get(0))<=0)
    	{
    		nextfirst.recIn(key,value,s);
    		return;
    	}
    	int i=1;
    	for(i=1;i<ar.size();i++)
		{
			if(key.compareTo(ar.get(i))<=0)
			{
				next.get(i-1).recIn(key, value, s);
				return;
			}
		}
    	next.get(i-1).recIn(key, value, s);
    }
	
	public void overflow(int m)
	{
		boolean leaf=false;
		if(nextfirst==null)
		{
			leaf=true;
		}
		Node<Key,Value> r=new Node<Key,Value>();
		Node<Key,Value> l=new Node<Key,Value>();
		int i=0;
		for(;i<m/2;i++)
		{
			l.ar.add(ar.get(i));
			l.arv.add(arv.get(i));
			l.next.add(next.get(i));
			if(!leaf)
			{
				l.next.get(i).prev=l;
			}
		}
		i++;
		for(;i<m;i++)
		{
			r.ar.add(ar.get(i));
			r.arv.add(arv.get(i));
			r.next.add(next.get(i));
			if(!leaf)
			{
				r.next.get(i-m/2-1).prev=r;
			}
		}
		l.nextfirst=nextfirst;
		r.nextfirst=next.get(m/2);
		if(!leaf)
		{
			l.nextfirst.prev=l;
			r.nextfirst.prev=r;
		}
		Key k=ar.get(m/2);
		Value v=arv.get(m/2);
		if(prev==null)
		{
			//System.out.println("yo");
			ar=new ArrayList<Key>();
			arv=new ArrayList<Value>();
			next=new ArrayList<Node<Key,Value>>();
			nextfirst=l;
			next.add(r);
			ar.add(k);
			arv.add(v);
			l.prev=this;
			r.prev=this;
		}
		else
		{
			//System.out.println(l.ar.toString()+r.ar.toString()+l.arv.toString()+r.arv.toString());
			Node<Key,Value> temp=this.prev;
			if(temp.nextfirst==this)
			{
				i=-1;
			}
			else 
			{
				i=0;
				while(temp.next.get(i)!=this)
				{
					i++;
				}
			}
			temp.ar.add(i+1,k);
			temp.arv.add(i+1,v);
			temp.next.add(i+1,r);
			r.prev=temp;
			l.prev=temp;
			if(i==-1)
			{
				temp.nextfirst=l;
			}
			else
			{
				temp.next.set(i, l);
			}
			//System.out.println(temp.nextfirst.ar.toString()+temp.nextfirst.arv.toString());
			if(temp.ar.size()==m)
			{
				temp.overflow(m);
			}
			return;
		}
	}
	
	public void recDelete(Key key,int m)
	{
		//Node<Key,Value> temp=this;
		if(ar.contains(key))
		{
			recurseD(key,m);
			return;
		}
		else
		{
			while(true) 
			{
				if(key.compareTo(ar.get(ar.size()-1))>0)
				{
					if(next.get(ar.size()-1).ar.size()==m/2-1)
					{
						Merge(ar.size()-1,m);
						if(ar.contains(key))
						{
							recurseD(key,m);
							return;
						}
						continue;
					}
					else
					{
						next.get(ar.size()-1).recDelete(key,m);
						return;
					}
				}
				for(int i=0;i<ar.size();i++)
				{
					if(key.compareTo(ar.get(i))<0)
					{
						if(i==0)
						{
							if(nextfirst.ar.size()==m/2-1)
							{
								Merge(-1,m);
								if(ar.contains(key))
								{
									recurseD(key,m);
									return;
								}
								break;
							}
							else
							{
								nextfirst.recDelete(key,m);
								return;
							}
						}
						if(next.get(i-1).ar.size()==m/2-1)
						{
							Merge(i-1,m);
							if(ar.contains(key))
							{
								recurseD(key,m);
								return;
							}
							break;
						}
						else
						{
							next.get(i-1).recDelete(key,m);
							return;
						}
					}
				}
			}
		}
	}
	
	public void Merge(int i, int m)
	{
		if(i==-1)
		{
			if(next.get(i+1).ar.size()>=m/2)
			{
				nextfirst.ar.add( ar.size()-1, ar.get(i+1));
				nextfirst.arv.add( ar.size()-1, arv.get(i+1));
				nextfirst.next.add(ar.size()-1, next.get(i+1).nextfirst);
				
				ar.set(i+1, next.get(i+1).ar.get(0));
				arv.set(i+1, next.get(i+1).arv.get(0));
				
				next.get(i+1).arv.remove(0);
				next.get(i+1).ar.remove(0);
				next.get(i+1).nextfirst=next.get(i+1).next.get(0);
				next.get(i+1).next.remove(0);
				return;
			}
		}
		else if(i==ar.size()-1)
		{
			if(i==0)
			{
				if(nextfirst.ar.size()>=m/2)
				{
					next.get(i).ar.add( 0, ar.get(i));
					next.get(i).arv.add( 0, arv.get(i));
					next.get(i).next.add( 0, next.get(i).nextfirst);
					next.get(i).nextfirst = (nextfirst.next.get(nextfirst.ar.size()-1));
					
					ar.set(i, nextfirst.ar.get(nextfirst.ar.size()-1));
					arv.set(i, nextfirst.arv.get(nextfirst.ar.size()-1));
					
					nextfirst.arv.remove(nextfirst.ar.size()-1);
					nextfirst.ar.remove(nextfirst.ar.size()-1);
					nextfirst.next.remove(nextfirst.ar.size()-1);
					return;
				}
			}
			else if(next.get(i-1).ar.size()>=m/2)
			{
				next.get(i).ar.add( 0, ar.get(i));
				next.get(i).arv.add( 0, arv.get(i));
				next.get(i).next.add( 0, next.get(i).nextfirst);
				next.get(i).nextfirst = (next.get(i-1).next.get(next.get(i-1).ar.size()-1));
				
				ar.set(i, next.get(i-1).ar.get(next.get(i-1).ar.size()-1));
				arv.set(i, next.get(i-1).arv.get(next.get(i-1).ar.size()-1));
				
				next.get(i-1).arv.remove(next.get(i-1).ar.size()-1);
				next.get(i-1).ar.remove(next.get(i-1).ar.size()-1);
				next.get(i-1).next.remove(next.get(i-1).ar.size()-1);
				return;
			}
		}
		else if(next.get(i-1).ar.size()>=m/2)
		{
			next.get(i).ar.add( 0, ar.get(i));
			next.get(i).arv.add( 0, arv.get(i));
			next.get(i).next.add( 0, next.get(i).nextfirst);
			next.get(i).nextfirst = (next.get(i-1).next.get(next.get(i-1).ar.size()-1));
			
			ar.set(i, next.get(i+1).ar.get(next.get(i-1).ar.size()-1));
			arv.set(i, next.get(i+1).arv.get(next.get(i-1).ar.size()-1));
				
			next.get(i-1).arv.remove(next.get(i-1).ar.size()-1);
			next.get(i-1).ar.remove(next.get(i-1).ar.size()-1);
			next.get(i-1).next.remove(next.get(i-1).ar.size()-1);
			return;
		}
		else if(next.get(i+1).ar.size()>=m/2)
		{
			nextfirst.ar.add( ar.size()-1, ar.get(i+1));
			nextfirst.arv.add( ar.size()-1, arv.get(i+1));
			nextfirst.next.add(ar.size()-1, next.get(i+1).nextfirst);
				
			ar.set(i+1, next.get(i+1).ar.get(0));
			arv.set(i+1, next.get(i+1).arv.get(0));
			
			next.get(i+1).arv.remove(0);
			next.get(i+1).ar.remove(0);
			next.get(i+1).nextfirst=next.get(i+1).next.get(0);
			next.get(i+1).next.remove(0);
			return;
		}
		Node<Key,Value> l;
		Node<Key,Value> r;
		if(i==-1||i==0)
		{
			i=0;
			l=nextfirst;
			r=next.get(0);	
		}
		else
		{
			l=next.get(i-1);
			r=next.get(i);
		}
		if(prev==null)
		{
			if(ar.size()==1)
			{
				Key k=ar.get(0);
				Value v=arv.get(0);
				nextfirst=l.nextfirst;
				ar=new ArrayList<Key>();
				arv=new ArrayList<Value>();
				next=new ArrayList<Node<Key,Value>>();
				next.addAll(l.next);
				next.add(r.nextfirst);
				next.addAll(l.next);
				ar.addAll(l.ar);
				ar.add(k);
				ar.addAll(r.ar);
				arv.addAll(l.arv);
				arv.add(v);
				arv.addAll(r.arv);
			}				
		}
		l.ar.add(ar.get(i));
		l.arv.add(arv.get(i));
		l.next.add(r.nextfirst);
		
		l.ar.addAll(r.ar);
		l.arv.addAll(r.arv);
		l.next.addAll(r.next);
		
		ar.remove(i);
		arv.remove(i);
		next.remove(i);
	}
	
	/*public void Merger(Key key,int m)
	{
		if(ar.size()>=m/2)
		{
			recurseD(key,m);
		}
		if(prev==null)
		{
			if(ar.size()>1)
			{
				
			}
		}
		
		boolean x=nextfirst==null;
		Node<Key,Value> temp=this.prev;
		int i=0;
		if(temp.nextfirst==this)
		{
			i=-1;
		}
		else while(this!=temp.next.get(i)) 
		{
			i++;
		}
		if(i==0 || i == -1)
		{
			if( (temp.nextfirst.ar.size()>1 && x) || temp.nextfirst.ar.size()>=m/2)
			{
				int ind=this.ar.indexOf(key);
				int j=nextfirst.ar.indexOf(ar.get(nextfirst.ar.size()-1));
				
				temp.next.get(0).ar.set(ind, temp.ar.get(0));
				temp.next.get(0).arv.set(ind, temp.arv.get(0));
				
				temp.ar.set(0, temp.nextfirst.ar.get(j));
				temp.arv.set(0, temp.nextfirst.arv.get(j));
				
				temp.nextfirst.recurseD(temp.nextfirst.ar.get(j),m);
				return;
			}
			else if((temp.next.get(0).ar.size()>1 && x) || temp.next.get(0).ar.size()>=m/2)
			{
				int ind=this.ar.indexOf(key);
				int j=0;
				
				temp.nextfirst.ar.set(ind, temp.ar.get(0));
				temp.nextfirst.arv.set(ind, temp.arv.get(0));
				
				temp.ar.set(0, next.get(0).ar.get(j));
				temp.arv.set(0, next.get(0).arv.get(j));
				
				temp.next.get(0).recurseD(temp.next.get(0).ar.get(j), m);
				return;
			}
			else if(i!=temp.ar.size()-1 && ((temp.next.get(1).ar.size()>1 && x) || temp.next.get(1).ar.size()>=m/2))
			{
				int ind=this.ar.indexOf(key);
				int j=0;
				
				temp.next.get(0).ar.set(ind, temp.ar.get(1));
				temp.next.get(0).arv.set(ind, temp.arv.get(1));
				
				temp.ar.set(1, temp.next.get(1).ar.get(j));
				temp.arv.set(1, temp.next.get(1).arv.get(j));
				
				temp.next.get(0).recurseD(next.get(i+1).ar.get(j), m);
			}
			else
			{
				temp.nextfirst.ar.add(temp.ar.get(0));
				temp.nextfirst.arv.add(temp.arv.get(0));
				temp.nextfirst.next.add(temp.nextfirst);
				temp.nextfirst.ar.addAll(temp.next.get(0).ar);
				temp.nextfirst.arv.addAll(temp.next.get(0).arv);
				temp.nextfirst.next.addAll(temp.next.get(0).next);
				temp.Merger(temp.ar.get(0),m);
				return;
			}
		}
		else
		{
			if( (temp.next.get(i-1).ar.size()>1 && x) || temp.next.get(i-1).ar.size()>=m/2)
			{
				int ind=this.ar.indexOf(key);
				int j=next.get(i-1).ar.indexOf(next.get(i-1).ar.get(ar.size()-1));
				
				temp.next.get(i).ar.set(ind, temp.ar.get(i));
				temp.next.get(i).arv.set(ind, temp.arv.get(i));
				
				temp.ar.set(i, temp.next.get(i-1).ar.get(j));
				temp.arv.set(i, temp.next.get(i-1).arv.get(j));
				
				temp.next.get(i-1).recurseD(temp.next.get(i-1).ar.get(j),m);
				return;
			}
			else if(i!=temp.ar.size()-1 &&((temp.next.get(i+1).ar.size()>1 && x) || temp.next.get(i+1).ar.size()>=m/2))
			{
				int ind=this.ar.indexOf(key);
				int j=0;
				
				temp.next.get(i).ar.set(ind, temp.ar.get(i+1));
				temp.next.get(i).arv.set(ind, temp.arv.get(i+1));
				
				temp.ar.set(i+1, temp.next.get(i+1).ar.get(j));
				temp.arv.set(i+1, temp.next.get(i+1).arv.get(j));
				
				temp.next.get(i+1).recurseD(next.get(i+1).ar.get(j), m);
				return;
			}
			else
			{
				 temp.Merger(key,m);
				 return;
			}
		}
	}*/
	
	public void recurseD(Key key,int m)
	{
		if(nextfirst==null)
		{
			if(ar.size()!=1)
			{
				int i=ar.indexOf(key);
				ar.remove(key);
				arv.remove(i);
				next.remove(i);
				return;
			}
			else if(prev==null)
			{
				ar.remove(0);
				arv.remove(0);
				next.remove(0);
				nextfirst=null;
				return;
			}
		}
		int i=ar.indexOf(key);
		if(i==0)
		{
			if( nextfirst.ar.size()>=m/2 )
			{
				ar.set(0, nextfirst.ar.get(nextfirst.ar.size() - 1));
				arv.set(0, nextfirst.arv.get(nextfirst.arv.size() - 1));
				nextfirst.recurseD(nextfirst.ar.get(nextfirst.ar.size()-1),m);
				return;
			}
			else if(next.get(0).ar.size()>=m/2)
			{
				ar.set(0, next.get(0).ar.get(0));
				arv.set(0, next.get(0).arv.get(0));
				nextfirst.recurseD(next.get(0).ar.get(0),m);
				return;
			}
			else
			{
				if(prev==null)
				{
					nextfirst.ar.add(ar.get(0));
					nextfirst.arv.add(arv.get(0));
					nextfirst.next.add(next.get(0).nextfirst);
					nextfirst.ar.addAll(next.get(0).ar);
					nextfirst.arv.addAll(next.get(0).arv);
					nextfirst.next.addAll(next.get(0).next);
					ar.remove(0);
					arv.remove(0);
					next.remove(0);
					ar.addAll(nextfirst.ar);
					arv.addAll(nextfirst.arv);
					next.addAll(nextfirst.next);
					nextfirst=nextfirst.nextfirst;
					recurseD(key,m);
				}
				else
				{
					nextfirst.ar.add(ar.get(0));
					nextfirst.arv.add(arv.get(0));
					nextfirst.next.add(next.get(0).nextfirst);
					nextfirst.ar.addAll(next.get(0).ar);
					nextfirst.arv.addAll(next.get(0).arv);
					nextfirst.next.addAll(next.get(0).next);
					ar.remove(0);
					arv.remove(0);
					next.remove(0);
					nextfirst.recurseD(key,m);
				}
				return;
			}
		}
		else
		{
			if(next.get(i-1).ar.size()>=m/2)
			{
				ar.set(i-1, next.get(i-1).ar.get(ar.size()-1));
				arv.set(i-1, next.get(i-1).arv.get(arv.size()-1));
				nextfirst.recurseD(next.get(i-1).ar.get(ar.size()-1),m);
				return;
			}
			else if(next.get(i).ar.size()>=m/2)
			{
				ar.set(i, next.get(i).ar.get(0));
				arv.set(i, next.get(i).arv.get(0));
				nextfirst.recurseD(next.get(i).ar.get(0),m);
				return;
			}
			else
			{
				next.get(i-1).ar.add(ar.get(i));
				next.get(i-1).arv.add(arv.get(i));
				next.get(i-1).next.add(next.get(i).nextfirst);
				next.get(i-1).ar.addAll(next.get(i).ar);
				next.get(i-1).arv.addAll(next.get(i).arv);
				next.get(i-1).next.addAll(next.get(i).next);
				ar.remove(i);
				arv.remove(i);
				next.remove(i);
				next.get(i-1).recurseD(key,m);
				return;
			}
		}
	}
}