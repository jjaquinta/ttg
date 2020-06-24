package jo.util.ui.swing.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;

public class ListenerUtils
{

    public static void listen(AbstractButton obj, final Consumer<ActionEvent> action)
    {
        obj.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void listen(JComboBox<?> obj, final Consumer<ActionEvent> action)
    {
        obj.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void listen(JSpinner obj, final Consumer<ChangeEvent> action)
    {
        obj.addChangeListener(new ChangeListener() {            
            @Override
            public void stateChanged(ChangeEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void onLost(JTextField obj, final Consumer<FocusEvent> handler)
    {
        obj.addFocusListener(new FocusAdapter() {            
            @Override
            public void focusLost(FocusEvent e)
            {
                handler.accept(e);
            }
        });
    }

    public static void keyTyped(JTextComponent obj, final Consumer<KeyEvent> action)
    {
        obj.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void keyPressed(JTextComponent obj, final Consumer<KeyEvent> action)
    {
        obj.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void keyReleased(JTextComponent obj, final Consumer<KeyEvent> action)
    {
        obj.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void change(JSpinner obj, final Consumer<ChangeEvent> action)
    {
        obj.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e)
            {
                action.accept(e);
            }
        });
    }

    public static void change(JTree obj, final Consumer<TreeSelectionEvent> action)
    {
        obj.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                action.accept(e);
            }
        });
    }
    
    public static void change(JList<?> obj, final Consumer<ListSelectionEvent> action)
    {
        obj.addListSelectionListener(new ListSelectionListener() {           
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                action.accept(e);
            }
        });
    }
    
    public static void change(JTable obj, final Consumer<ListSelectionEvent> action)
    {
        obj.getSelectionModel().addListSelectionListener(new ListSelectionListener() {           
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                action.accept(e);
            }
        });
    }
}
