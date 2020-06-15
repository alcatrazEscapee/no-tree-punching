#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Copyright (c) 2019. See the project LICENSE.md for details.

from mcresources import ResourceManager


def generate(rm: ResourceManager):
    # First
    rm.lang({
        'itemGroup.notreepunching.items': 'No Tree Punching'
    })

    # Stone
    for stone in ('granite', 'andesite', 'diorite'):
        rm.blockstate('%s_cobblestone' % stone) \
            .with_block_model() \
            .with_item_model() \
            .with_tag('cobblestone') \
            .with_block_loot('notreepunching:%s_cobblestone' % stone) \
            .with_lang(lang('%s cobblestone', stone)) \
            .make_stairs() \
            .make_slab() \
            .make_wall()
        for piece in ('stairs', 'slab', 'wall'):
            rm.block('%s_cobblestone_%s' % (stone, piece)) \
                .with_lang(lang('%s cobblestone %s', stone, piece)) \
                .with_tag('minecraft:' + piece + ('s' if not piece.endswith('s') else ''))  # plural tag

    for stone in ('granite', 'andesite', 'diorite', 'stone', 'sandstone', 'red_sandstone'):
        rm.blockstate('%s_loose_rock' % stone) \
            .with_block_model(textures='minecraft:block/%s' % stone, parent='notreepunching:block/loose_rock') \
            .with_block_loot('notreepunching:%s_loose_rock' % stone) \
            .with_tag('loose_rocks') \
            .with_lang(lang('%s loose rock', stone))
        # flat item model for the block item
        rm.item_model('%s_loose_rock' % stone)

    # Pottery
    for pottery in ('worked', 'large_vessel', 'small_vessel', 'bucket', 'flower_pot'):
        block = rm.blockstate('clay_%s' % pottery) \
            .with_block_model(textures='minecraft:block/clay', parent='notreepunching:block/pottery_%s' % pottery) \
            .with_item_model() \
            .with_block_loot('notreepunching:clay_%s' % pottery)
        if pottery == 'worked':
            block.with_lang(lang('worked clay'))
        else:
            block.with_lang(lang('clay %s', pottery))

    rm.blockstate('ceramic_large_vessel') \
        .with_block_model(textures='notreepunching:block/ceramic', parent='notreepunching:block/pottery_large_vessel') \
        .with_item_model() \
        .with_block_loot('notreepunching:ceramic_large_vessel') \
        .with_lang(lang('ceramic large vessel'))

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        rm.item_model('%s_mattock' % tool) \
            .with_lang(lang('%s mattock', tool))
        rm.item_model('%s_saw' % tool) \
            .with_lang(lang('%s saw', tool)) \
            .with_tag('saws')
        rm.item_model('%s_knife' % tool) \
            .with_lang(lang('%s knife', tool)) \
            .with_tag('knives')

    # Flint
    for tool in ('axe', 'pickaxe', 'shovel', 'hoe', 'knife'):
        rm.item_model('flint_%s' % tool) \
            .with_lang(lang('flint %s', tool))
    rm.item_model('macuahuitl') \
        .with_lang(lang('macuahuitl'))
    rm.item('flint_knife').with_tag('knives')

    for item in ('flint_shard', 'plant_fiber', 'plant_string', 'clay_brick', 'ceramic_small_vessel', 'clay_tool', 'fire_starter'):
        rm.item_model(item) \
            .with_lang(lang(item))

    # ceramic bucket, since it uses a very custom model
    rm.data(('models', 'item', 'ceramic_bucket'), {
        'parent': 'forge:item/default',
        'textures': {
            'base': 'notreepunching:item/ceramic_bucket',
            'fluid': 'forge:item/mask/bucket_fluid_drip'
        },
        'loader': 'forge:bucket',
        'fluid': 'empty'
    }, root_domain='assets')
    rm.item('ceramic_bucket').with_lang(lang('ceramic bucket'))

    # Misc Tags
    rm.item('plant_string').with_tag('string')
    rm.item('minecraft:gravel').with_tag('always_breakable').with_tag('always_drops')
    for wood in ('acacia', 'oak', 'dark_oak', 'jungle', 'birch', 'spruce'):
        rm.item('minecraft:%s_leaves' % wood).with_tag('always_breakable').with_tag('always_drops')


def generate_vanilla(rm: ResourceManager):
    # Vanilla Tags
    rm.item('flint').with_tag('notreepunching:flint_knappable')
    for block in ('grass_block', 'dirt', 'coarse_dirt', 'gravel', 'sand', 'red_sand', 'terracotta', 'stone', 'andesite', 'diorite', 'granite', 'sandstone', 'red_sandstone', 'podzol'):
        rm.block(block).with_tag('notreepunching:loose_rock_placeable_on')


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
