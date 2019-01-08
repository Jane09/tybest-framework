package com.tybest.base.tree;

/**
 * B-Tree/B树 横杆不是 减，不是二叉
 * 为了描述B-Tree，首先定义一条数据记录为一个二元组[key, data]，key为记录的键值，对于不同数据记录，key是互不相同的；data为数据记录除key外的数据。那么B-Tree是满足下列条件的数据结构：
 * d为大于1的一个正整数，称为B-Tree的度。
 * h为一个正整数，称为B-Tree的高度。
 * 每个非叶子节点由n-1个key和n个指针组成，其中d<=n<=2d。
 * 每个叶子节点最少包含一个key和两个指针，最多包含2d-1个key和2d个指针，叶节点的指针均为null 。
 * 所有叶节点具有相同的深度，等于树高h。
 * key和指针互相间隔，节点两端是指针。
 * 一个节点中的key从左到右非递减排列。
 * 所有节点组成树结构。
 * 每个指针要么为null，要么指向另外一个节点。
 * 如果某个指针在节点node最左边且不为null，则其指向节点的所有key小于v(key1)，其中v(key1)为node的第一个key的值。
 * 如果某个指针在节点node最右边且不为null，则其指向节点的所有key大于v(keym)，其中v(keym)为node的最后一个key的值。
 * 如果某个指针在节点node的左右相邻key分别是keyi和keyi+1且不为null，则其指向节点的所有key小于v(keyi+1)且大于v(keyi)。
 *
 *
 * B+Tree
 * 与B-Tree相比，B+Tree有以下不同点：
 * 每个节点的指针上限为2d而不是2d+1。
 * 内节点不存储data，只存储key；叶子节点不存储指针。
 *
 * 二叉搜索树
 *
 * 红黑树
 *
 * B*Tree
 * @author tb
 * @date 2019/1/8 17:05
 */
public class BTree {

    public static void main(String[] args) {

    }
}
